package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.common.exception.BussinessException;
import com.adpanshi.cashloan.common.exception.ManageException;
import com.adpanshi.cashloan.manage.arc.model.SysUser;
import com.adpanshi.cashloan.manage.cl.mapper.*;
import com.adpanshi.cashloan.manage.cl.model.*;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowMainModel;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowModel;
import com.adpanshi.cashloan.manage.cl.model.expand.PayLogModel;
import com.adpanshi.cashloan.manage.cl.service.BorrowMainService;
import com.adpanshi.cashloan.manage.cl.service.NoticesService;
import com.adpanshi.cashloan.manage.cl.service.SmsService;
import com.adpanshi.cashloan.manage.core.common.cache.RedissonClientUtil;
import com.adpanshi.cashloan.manage.core.common.context.Global;
import com.adpanshi.cashloan.manage.core.common.util.DateUtil;
import com.adpanshi.cashloan.manage.core.common.util.StringUtil;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.redisson.core.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Vic Tang
 * @Description: 借款主订单serviceImpl
 * @date 2018/8/2 11:49
 */
@Service("borrowMainServiceImpl")
public class BorrowMainServiceImpl implements BorrowMainService{
    private static final Logger logger = LoggerFactory.getLogger(BorrowMainServiceImpl.class);
    @Resource
    private BorrowMainMapper borrowMainMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private BorrowRepayLogMapper borrowRepayLogMapper;
    @Resource
    private BorrowAuditLogMapper borrowAuditLogMapper;
    @Resource
    private BorrowMainProgressMapper borrowMainProgressMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private BankCardMapper bankCardMapper;
    @Resource
    private PayLogMapper payLogMapper;
    @Resource
    private SmsService smsService;
    @Resource
    private NoticesService noticesService;

    @Override
    public List<BorrowMainModel> selectBorrowList(Map<String, Object> searchParams, int current, int pageSize) {
        if(current !=0 && pageSize !=0) {
            PageHelper.startPage(current, pageSize);
        }
        List<BorrowMainModel> borrowMains = borrowMainMapper.selectBorrowList(searchParams);
        List<Channel> channelList = channelMapper.listChannel();
        //渠道名称
        for (BorrowMainModel borrowMainModel : borrowMains){
            for (Channel channel : channelList){
                Long channelId = borrowMainModel.getChannelId();
                if (null != channelId){
                    if (borrowMainModel.getChannelId().compareTo(channel.getId()) == 0){
                        borrowMainModel.setChannelName(channel.getName());
                    }
                }
            }
        }
        return borrowMains;
    }

    @Override
    public Map qryRepayLog(Long id) {
        return borrowRepayLogMapper.qryRepayLog(id);
    }

    @Override
    public BorrowMain getById(Long id) {
        return borrowMainMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updatePayState(Long borrowId, String state, Date loanTime, Date repayTime){
        BorrowMain borrowMain = new BorrowMain();
        borrowMain.setId(borrowId);
        borrowMain.setState(state);
        if (loanTime != null) {
           borrowMain.setLoanTime(loanTime);
        }
        if (repayTime != null) {
            borrowMain.setRepayTime(repayTime);
        }
        int result  =  borrowMainMapper.updateByPrimaryKeySelective(borrowMain);
        if(result < 1){
            throw new BussinessException("当前借款状态不允许修改");
        }
    }

    @Override
    public Map<String, Object> orderAllotSysUsers(SysUser sysUser, Long[] borrowMainIds, Long userId, String userName) {
        Date dt = new Date();
        String strDate = DateUtil.dateToString(dt, DateUtil.yyyyMMddHHmmss);
        String jsonData = "[操作人ID:"+sysUser.getId()+",NAME:"+sysUser.getName()+",DATE:"+strDate+"]";
        Map<String,Object> outMap = new HashMap<>();
        List succList = new ArrayList();
        List failList = new ArrayList();
        for (Long borrowMainId : borrowMainIds) {
            //订单只有待审核的状态才能分配
            BorrowMain borrowMain = borrowMainMapper.selectByPrimaryKey(borrowMainId);
            String borrowState = borrowMain.getState();
            //只允许待审核的状态.重新分配
            if (BorrowModel.STATE_NEED_REVIEW.equals(borrowState) || BorrowModel.STATE_HANGUP.equals(borrowState)){
                BorrowAuditLogWithBLOBs borrowAuditLog = new BorrowAuditLogWithBLOBs();
                BorrowAuditLogExample example = new BorrowAuditLogExample();
                example.createCriteria().andBorrowIdEqualTo(borrowMainId);
                borrowAuditLog.setJsonData(jsonData+",[OLD->ID:"+borrowMain.getSysUserId()+",NAME:"+borrowMain.getSysUserName()+",DATE:"+DateUtil.dateToString(borrowMain.getSysCreateTime(),DateUtil.yyyyMMddHHmmss)
                        +",NEW->ID:"+userId+",NAME:"+userName+",DATE:"+strDate+"]");
                borrowAuditLogMapper.updateByExampleWithBLOBs(borrowAuditLog,example);
                borrowMain.setSysUserId(userId);
                borrowMain.setSysUserName(userName);
                borrowMain.setSysCreateTime(dt);
                borrowMainMapper.updateByPrimaryKeySelective(borrowMain);
                succList.add(borrowMain);
            }else{
                failList.add(borrowMain);
            }
        }
        outMap.put("succList",succList);
        outMap.put("failList",failList);
        return outMap;
    }

    @Override
    public int modifyStateAndRemark(long id, String state, String remark) {
    //审核步骤:
        BorrowAuditLogWithBLOBs borrowAuditLog = new BorrowAuditLogWithBLOBs();
        BorrowAuditLogExample example = new BorrowAuditLogExample();
        example.createCriteria().andBorrowIdEqualTo(id);
        String strData = DateUtil.dateStr2(new Date());
        StringBuffer sb = new StringBuffer(strData);
        BorrowMain main = new BorrowMain();
        main.setState(state);
        main.setId(id);
        /*平均分配:待人审状态-> 自动分配
        * @param borrowMainId
        * @date : 2018-1-04 15:58
        * @author: nmnl*/
        if (state.equals(BorrowModel.STATE_NEED_REVIEW)){
            Map<String, Object> ueMap = new HashMap<>();
            //减少查询范围:获取正在启用的信审人员,已经分配了多少订单
            ueMap.put("stateList",Arrays.asList(BorrowModel.STATE_NEED_REVIEW,BorrowModel.STATE_HANGUP));
            ueMap.put("startTime",strData+" 00:00:00");
            ueMap.put("endTime",strData+" 23:59:59");
            BorrowMainModel borrowMainModel = borrowMainMapper.selectBorrowSysCount(ueMap);
            if(StringUtil.isNotEmptys(borrowMainModel)){
                //当前订单自动分配给检索出来的信审人员.
                main.setSysUserId(borrowMainModel.getSystemId());
                main.setSysUserName(borrowMainModel.getSystemName());
                main.setSysCreateTime(new Date());
                sb.append(" 自动分配BorrowMainId: ").append(id).append(" 分配订单状态: ").append(state).append(" 分配人ID: ").append(borrowMainModel.getSystemId()).append(" 分配人名称: ").append(borrowMainModel.getSystemName());
            }
        }
        //没有remark字段.是除了22状态之外的.
        if(StringUtils.isNotEmpty(remark)){
            sb.append(" 审核: ").append(remark);
            main.setRemark(sb.toString());
        }
        borrowAuditLog.setJsonData(sb.toString());
        borrowAuditLogMapper.updateByExampleWithBLOBs(borrowAuditLog,example);
        return borrowMainMapper.updateByPrimaryKeySelective(main);
    }

    @Override
    public String manualVerifyBorrow(Long borrowMainId, String state, String remark, Long userId, String realName, String orderView) throws ManageException{
//修改返回值为订单状态，如果返回为空，则代表修改不成功；否则返回对应的状态值 @author yecy 20180302
        String resultState = "";
        BorrowMain borrowMain = borrowMainMapper.selectByPrimaryKey(borrowMainId);
        if(null == borrowMain) {
            throw new ManageException(ManageExceptionEnum.MANAGE_LOAN_OTHER_NOT_BORROW);
        }
        Long borrowId = borrowMain.getId();
        String borrowMainRemark = borrowMain.getRemark();
        //待审核22 -> 人审挂起25 -> 人审通过26 -> 放款成功30／放款失败31
        //待审核22 -> 人审挂起25 -> 人审通过27
        String borrowMainState = borrowMain.getState();
        //校验当前传入状态.只有这些状态才允许处理.否则不允许
        /*if (state.equals(BorrowModel.STATE_HANGUP) || state.equals(BorrowModel.STATE_PASS) || state.equals(BorrowModel.STATE_REFUSED)){*/
        String newRemark = "borrowMainId: " + borrowMainId + " 现在状态: " + borrowMainState + " 更改为: " + state + " 操作人ID: " + userId + " 操作人名称: " +realName + " 订单原备注" + borrowMainRemark;
        //初始化状态 [待审核22] [人审挂起25]
        if (borrowMainState.equals(BorrowModel.STATE_NEED_REVIEW) || borrowMainState.equals(BorrowModel.STATE_HANGUP)){
            //人审挂起
            if (state.equals(BorrowModel.STATE_HANGUP)){
                //修改主订单状态
                modifyStateAndRemark(borrowId,BorrowModel.STATE_HANGUP,newRemark + " 审核数据 " +remark);
                //增加进程
                BorrowMainProgress borrowProgress = new BorrowMainProgress();
                borrowProgress.setBorrowId(borrowMain.getId());
                borrowProgress.setUserId(borrowMain.getUserId());
                if (state.equals(BorrowModel.STATE_PRE)) {
                    borrowProgress.setRemark("Borrowing ₹"
                            + StringUtil.isNull(borrowMain.getAmount())
                            + ",Tenure of lending "
                            + borrowMain.getTimeLimit()
                            + " days,Comprehensive cost ₹"
                            + StringUtil.isNull(borrowMain.getFee()));
                } else {
                    borrowProgress.setRemark(BorrowModel.convertBorrowRemark(state));
                }
                borrowProgress.setState(state);
                borrowProgress.setCreateTime(DateUtil.getNow());
                borrowMainProgressMapper.insertSelective(borrowProgress);
                resultState = state;
            }else if(state.equals(BorrowModel.STATE_PASS)){//人审通过
                //修改主订单状态
                if (StringUtils.isNotEmpty(remark)) {
                    modifyStateAndRemark(borrowId, state, borrowMain.getRemark()!=null?borrowMain.getRemark()+""
                            + " ;人审备注：" + remark:"人审备注：" + remark);
                } else {
                    modifyStateAndRemark(borrowId, state,null);
                }
                BorrowMainProgress borrowProgress = new BorrowMainProgress();
                borrowProgress.setBorrowId(borrowMain.getId());
                borrowProgress.setUserId(borrowMain.getUserId());
                if (state.equals(BorrowModel.STATE_PRE)) {
                    borrowProgress.setRemark("Borrowing ₹"
                            + StringUtil.isNull(borrowMain.getAmount())
                            + ",Tenure of lending "
                            + borrowMain.getTimeLimit()
                            + " days,Comprehensive cost ₹"
                            + StringUtil.isNull(borrowMain.getFee()));
                } else {
                    borrowProgress.setRemark(BorrowModel.convertBorrowRemark(state));
                }
                borrowProgress.setState(state);
                borrowProgress.setCreateTime(DateUtil.getNow());
                borrowMainProgressMapper.insertSelective(borrowProgress);
                resultState = state;
                // 人审通过后的自动放款去除，改为经直融后，满标放款 @authortq 20180732
                borrowLoan(borrowMain,new Date());
            }else if (state.equals(BorrowModel.STATE_REFUSED)){//人审拒绝
//                //停用当前合同模版.
//                userAuthService.updateLoanSceneStateRefusedWithLoanScene(Integer.parseInt(state), borrowMain.getUserId(), orderView);
                refused(borrowMain, newRemark + " 审核数据 " +remark, null,true);
                resultState = state;
            }
        }
        return resultState;
    }

    public void borrowLoan(BorrowMain borrow, Date date) {
        Long userId = borrow.getUserId();
        User user = userMapper.selectByPrimaryKey(userId);
        // 用户为有效用户时，才进行放款操作 @author yecy 20180417
        if (user == null || user.getState() != 1) {
            throw new BussinessException("当前用户为无效用户,不允许放款。");
        }
        //@remarks:增加用户订单锁.当前用户本次放款等待.阻塞并发的产生. @date:20170915 @author:nmnl
        Long borrowId = borrow.getId();
        RLock rLock = RedissonClientUtil.getRedisLock(this, "borrowLoan", borrowId);
        try {
            rLock.lock(100, TimeUnit.SECONDS);
            //@remarks: 如果用户存在未还款的订单,不允许放款.  @date: 20170907 @author: nmnl
            List<String> stateList = Arrays.asList(BorrowModel.STATE_REPAY,BorrowModel.STATE_DELAY, BorrowModel.STATE_BAD);
            BorrowMainExample example = new BorrowMainExample();
            example.createCriteria().andUserIdEqualTo(userId).andStateIn(stateList);
            example.setOrderByClause("id desc");
            BorrowMain unRepayBorrow = new BorrowMain();
             List<BorrowMain> infos = borrowMainMapper.selectByExample(example);
            if (infos.size() > 0) {
                logger.error(" 用户已经存在一个待还款订单不允许第二次放款userId: " + userId + " 已存在未还的borrowId: " + unRepayBorrow.getId() + " 申请放款的borrowId: " + borrowId);
                return;
            }
            //@remarks: 只有真正放款.才会更改额度.移植到payController paymentNotify  @date: 20170724 @author: nmnl
            // 调用连连支付实时付款进行放款
            BankCardExample example1 = new BankCardExample();
            example1.createCriteria().andUserIdEqualTo(borrow.getUserId());
            BankCard bankCard = new BankCard();
            List<BankCard> info1s = bankCardMapper.selectByExample(example1);
            if(info1s.size() > 0){
                bankCard = info1s.get(0);
                String orderNo = borrow.getOrderNo();
                PayLog payLog = new PayLog();
                payLog.setOrderNo(orderNo);
                payLog.setUserId(borrow.getUserId());
                // 下单修改为存储borrowMainId字段 @author yecy 20171214
                payLog.setBorrowMainId(borrow.getId());
                payLog.setAmount(borrow.getRealAmount());
                payLog.setCardNo(bankCard.getCardNo());
                payLog.setBank(bankCard.getBank());
                payLog.setSource(PayLogModel.SOURCE_FUNDS_OWN);
                payLog.setType(PayLogModel.TYPE_PAYMENT);
                payLog.setScenes(PayLogModel.SCENES_LOANS);
                payLog.setState(PayLogModel.STATE_PAYMENT_WAIT);
                payLog.setRemark("模拟交易成功");
                payLog.setPayReqTime(date);
                payLog.setCreateTime(DateUtil.getNow());
                payLogMapper.insertSelective(payLog);
            } else {
                throw new BussinessException("当前用户没有绑定银行卡,不允许放款。");
            }
//            UserBaseInfoExample example2 = new UserBaseInfoExample();
//            example2.createCriteria().andUserIdEqualTo(borrow.getUserId());
//            UserBaseInfo baseInfo = userBaseInfoMapper.selectByExample(example2).get(0);

//            PaymentModel payment = new PaymentModel(orderNo);
//
//            //查询同订单付款失败的第一笔记录（首笔记录）的支付时间[同订单再次发起请求时所有参数都必需保持一致]
//            date = getDtOrderDate(orderNo, date);
//            payment.setDt_order(DateUtil.dateStr3(date));
////            if ("dev".equals(Global.getValue("app_environment"))) {
////                payment.setMoney_order("0.01");
////            } else {
//            payment.setMoney_order(StringUtil.isNull(borrow.getRealAmount()));
////            }
//            payment.setAmount(borrow.getRealAmount());
//            payment.setCard_no(bankCard.getCardNo());
//            payment.setAcct_name(baseInfo.getRealName());
//            payment.setInfo_order(borrow.getOrderNo() + "付款");
//            payment.setMemo(baseInfo.getPhone() + "付款");
//            payment.setNotify_url(Global.getValue("server_host") + "/pay/lianlian/paymentNotify.htm");
//            payment.setOid_partner(Global.getValue(LianLianConstant.BUSINESS_NO_R));
//            LianLianHelper helper = new LianLianHelper();
//            payment = (PaymentModel) helper.newPayment(payment);
            // 跑单元测试时-放开此行代码
            //payment = (PaymentModel)new borrowLoansFacade.payment(payment);


//            if (payment.checkReturn()) { // 已生成连连支付单，付款处理中（交易成功，不是指付款成功，是指流程正常）

//            } else if ("4002".equals(payment.getRet_code())
//                    || "4003".equals(payment.getRet_code())
//                    || "4004".equals(payment.getRet_code())) { // 疑似重复订单，待人工审核
//                payLog.setState(PayLogModel.STATE_PENDING_REVIEW);
//                payLog.setConfirmCode(payment.getConfirm_code());
//                payLog.setUpdateTime(DateUtil.getNow());
//            } else if ("4006".equals(payment.getRet_code()) // 敏感信息加密异常
//                    || "4007".equals(payment.getRet_code()) // 敏感信息解密异常
//                    || "4009".equals(payment.getRet_code())) { // 验证码异常
//                payLog.setState(PayLogModel.STATE_PAYMENT_WAIT);
//            } else {
//                payLog.setState(PayLogModel.STATE_PAYMENT_FAILED);
//                payLog.setUpdateTime(DateUtil.getNow());

//                // 放款失败，更新borrow状态
//                borrowMainService.modifyState(borrow.getId(), BorrowModel.STATE_REPAY_FAIL);
//            }


        } finally {
            rLock.unlock();
        }
    }

    private BorrowMain refused(BorrowMain borrow, String remark, String operate, Boolean isManual) {
        String autoRefusedState ;
        if (isManual){ //是否为人工操作
            autoRefusedState  = BorrowModel.STATE_REFUSED;
        }else {
            autoRefusedState  = BorrowModel.STATE_AUTO_REFUSED;
        }
        Long borrowMainId = borrow.getId();
        Long userId = borrow.getUserId();
        if (StringUtils.isEmpty(remark)) {
            modifyStateAndRemark(borrowMainId,autoRefusedState,null);
        } else {
            modifyStateAndRemark(borrowMainId, autoRefusedState, remark);
        }
        if ("reVerify".equals(operate)) {
            BorrowMainProgressExample example = new BorrowMainProgressExample();
            example.createCriteria().andUserIdEqualTo(borrow.getUserId()).andBorrowIdEqualTo(borrowMainId);
            example.setOrderByClause("id desc");
            BorrowMainProgress borrowProgress = new BorrowMainProgress();
            List<BorrowMainProgress> infos = borrowMainProgressMapper.selectByExample(example);
            if(infos.size() > 0){
                borrowProgress = infos.get(0);
                if (autoRefusedState.equals(BorrowModel.STATE_PRE)) {
                    borrowProgress.setRemark("借款"
                            + StringUtil.isNull(borrow.getAmount())
                            + "元，期限"
                            + borrow.getTimeLimit()
                            + "天，综合费用"
                            + StringUtil.isNull(borrow.getFee()) + "元，"
                            + BorrowModel.convertBorrowRemark(autoRefusedState));
                } else {
                    borrowProgress.setRemark(BorrowModel.convertBorrowRemark(autoRefusedState));
                }
                borrowProgress.setState(autoRefusedState);
                borrowProgress.setCreateTime(DateUtil.getNow());
                borrowMainProgressMapper.updateByPrimaryKeySelective(borrowProgress);
            }
        } else {
            BorrowMainProgress borrowProgress = new BorrowMainProgress();
            borrowProgress.setBorrowId(borrow.getId());
            borrowProgress.setUserId(borrow.getUserId());
            if (autoRefusedState.equals(BorrowModel.STATE_PRE)) {
                borrowProgress.setRemark("Borrowing ₹"
                        + StringUtil.isNull(borrow.getAmount())
                        + ",Tenure of lending "
                        + borrow.getTimeLimit()
                        + " days,Comprehensive cost ₹"
                        + StringUtil.isNull(borrow.getFee()));
            } else {
                borrowProgress.setRemark(BorrowModel.convertBorrowRemark(autoRefusedState));
            }
            borrowProgress.setState(autoRefusedState);
            borrowProgress.setCreateTime(DateUtil.getNow());
            borrowMainProgressMapper.insertSelective(borrowProgress);
        }
        Integer againBorrow = Global.getInt("again_borrow");
        Date dt = new Date();
        int day = DateUtil.daysBetween(borrow.getCreateTime(),dt);
        day = againBorrow - day;
//        //审核不通过发送短信通知
        smsService.refuse(userId,day,borrow.getOrderNo());
//        //审核不通过消息通知
        noticesService.refuse(userId,day);
        borrow.setState(autoRefusedState);

        return borrow;
    }
}
