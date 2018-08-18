package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.business.user.domain.UserDataPackageDomain;
import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.common.exception.BussinessException;
import com.adpanshi.cashloan.common.exception.ManageException;
import com.adpanshi.cashloan.manage.arc.mapper.CreditMapper;
import com.adpanshi.cashloan.manage.arc.model.Credit;
import com.adpanshi.cashloan.manage.arc.model.CreditExample;
import com.adpanshi.cashloan.manage.cl.enums.BorrowRepayEnum;
import com.adpanshi.cashloan.manage.cl.mapper.*;
import com.adpanshi.cashloan.manage.cl.model.*;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowModel;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowRepayModel;
import com.adpanshi.cashloan.manage.cl.model.expand.UrgeRepayOrderModel;
import com.adpanshi.cashloan.manage.cl.pojo.*;
import com.adpanshi.cashloan.manage.cl.service.BorrowRepayService;
import com.adpanshi.cashloan.manage.cl.service.NoticesService;
import com.adpanshi.cashloan.manage.cl.service.SmsService;
import com.adpanshi.cashloan.manage.core.common.context.ExportConstant;
import com.adpanshi.cashloan.manage.core.common.util.StringUtil;
import com.adpanshi.cashloan.manage.core.common.util.excel.ReadExcelUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tool.util.DateUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Vic Tang
 * @Description: 还款计划serviceImpl
 * @date 2018/7/31 17:31
 */
@Service("borrowRepayService")
public class BorrowRepayServiceImpl implements BorrowRepayService{
    private static final Logger logger = LoggerFactory.getLogger(BorrowRepayServiceImpl.class);
    @Resource
    private BorrowRepayMapper borrowRepayMapper;
    @Resource
    private BorrowMapper borrowMapper;
    @Resource
    private CreditMapper creditMapper;
    @Resource
    private UrgeRepayOrderMapper urgeRepayOrderMapper;
    @Resource
    private UrgeRepayLogMapper urgeRepayLogMapper;
    @Resource
    private SmsService smsService;
    @Resource
    private NoticesService noticesService;
    @Resource
    private BorrowProgressMapper borrowProgressMapper;
    @Resource
    private BorrowMainMapper borrowMainMapper;
    @Resource
    private BorrowRepayLogMapper borrowRepayLogMapper;
    @Resource
    private BorrowMainProgressMapper borrowMainProgressMapper;
    @Resource
    private UserDataPackageDomain userDataPackageDomain;

    @Override
    public Page<BorrowRepayModel> listModel(Map<String, Object> params, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        return (Page<BorrowRepayModel>) borrowRepayMapper.listModelNew(params);
    }

    @Override
    public void confirmRepayNew(Map<String, Object> param) throws Exception {
        logger.debug("确认还款...");
        Long id = (Long) param.get("id");
        BorrowRepay br = borrowRepayMapper.selectByPrimaryKey(id);
        String state = (String) param.get("state");
        Date repayTime = (Date) param.get("repayTime");
        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd");
        Date repayPlanTime1 = DateUtil.valueOf(time1.format(br.getRepayTime()));//计划还款日期
        Date repay_time1 = DateUtil.valueOf(time1.format(repayTime));//还款时间
        Date today_time = DateUtil.valueOf(time1.format(new Date()));//今日日期
        BigDecimal amount = param.get("amount") != null ? new BigDecimal(String.valueOf(param.get("amount"))) : BigDecimal.ZERO;
        BigDecimal penaltyAmoutIn = param.get("penaltyAmout") != null ? new BigDecimal(String.valueOf(param.get("penaltyAmout"))) : BigDecimal.ZERO;
        if (BorrowRepayModel.NORMAL_REPAYMENT.equals(state)) {
            //正常还款
            //还款时间已逾期
            if (repay_time1.after(repayPlanTime1)) {
                throw new ManageException(ManageExceptionEnum.CLIENT_EXCEPTION_CODE_VALUE);
            }
            //还款金额不匹配
            if (amount.compareTo(br.getAmount()) !=0) {
                throw new ManageException(ManageExceptionEnum.MANAGE_REPAY_REPAY_AMOUNT_ERROR_CODE_VALUE);
            }
            state = BorrowModel.STATE_FINISH;
            param.put("penaltyDay", String.valueOf(0));
            param.put("penaltyAmout", 0.00);
        } else if (BorrowRepayModel.OVERDUE_REPAYMENT.equals(state)) {
            //还款时间还未逾期
            if (!repay_time1.after(repayPlanTime1)) {
                throw new ManageException(ManageExceptionEnum.MANAGE_REPAY_NOT_OVERDUE_CODE_VALUE);
            }
            //还款金额不匹配
            if (amount.compareTo(br.getAmount()) != 0) {
                throw new ManageException(ManageExceptionEnum.MANAGE_REPAY_REPAY_AMOUNT_ERROR_CODE_VALUE);
            }
            state = BorrowModel.STATE_FINISH;
            //逾期还款
            if (repay_time1.equals(today_time)) {
                //逾期利息不匹配应还利息
                if (penaltyAmoutIn.compareTo( br.getPenaltyAmout()) != 0) {
                    throw new ManageException(ManageExceptionEnum.MANAGE_REPAY_INTREST_ERROR_CODE_VALUE);
                }
                param.put("penaltyAmout", br.getPenaltyAmout());
                param.put("penaltyDay", br.getPenaltyDay());
            } else {
                //非当天的做逾息计算
                BigDecimal penaltyAmout = br.getPenaltyAmout();
                Integer penaltyDay = br.getPenaltyDay();
                BigDecimal simplyAmout = penaltyAmout.divide(new BigDecimal(penaltyDay));
                long day = (repay_time1.getTime() - repayPlanTime1.getTime()) / (24 * 60 * 60 * 1000);
                BigDecimal sum = simplyAmout.multiply(new BigDecimal(day));
                //逾期利息不匹配应还利息
                if (penaltyAmoutIn.compareTo(sum) != 0) {
                    throw new ManageException(ManageExceptionEnum.MANAGE_REPAY_INTREST_ERROR_CODE_VALUE);
                }
                param.put("penaltyAmout", sum);
                param.put("penaltyDay", String.valueOf(day));
            }
        } else if (BorrowRepayModel.OVERDUE_RELIEF.equals(state)) {
            //逾期减免
            state = BorrowModel.STATE_REMISSION_FINISH;
            //还款时间还未逾期
            if (!repay_time1.after(repayPlanTime1)) {
                throw new ManageException(ManageExceptionEnum.MANAGE_REPAY_NOT_OVERDUE_CODE_VALUE);
            }
            //还款金额不能大于应还金额
            if (br.getAmount().compareTo(amount) == -1) {
                throw new ManageException(ManageExceptionEnum.MANAGE_REPAY_MORE_REPAY_AMOUNT_CODE_VALUE);
            }
            //减免金额
            BigDecimal derateAmount = param.get("derateAmount") != null ? new BigDecimal(String.valueOf(param.get("derateAmount"))) : BigDecimal.ZERO;
            if (repay_time1.equals(today_time)) {
                //当天还款取还款计划表中数据
                BigDecimal difAmount = penaltyAmoutIn.add(derateAmount).add(amount).subtract(br.getPenaltyAmout()).subtract(br.getAmount());
                //实际还款+减免金额与应还金额不匹配
                if (difAmount.compareTo(BigDecimal.ZERO) != 0) {
                    throw new ManageException(ManageExceptionEnum.MANAGE_REPAY_REMISSION_AMOUNT_ERROR_CODE_VALUE);
                }
                param.put("penaltyDay", br.getPenaltyDay());
                param.put("overduePenaltyAmout", br.getPenaltyAmout());
            } else {
                //非当天更新逾期日期
                BigDecimal penaltyAmout = br.getPenaltyAmout();
                Integer penaltyDay = br.getPenaltyDay();
                BigDecimal simplyAmout = penaltyAmout.divide(new BigDecimal(penaltyDay));
                long day = (repay_time1.getTime() - repayPlanTime1.getTime()) / (24 * 60 * 60 * 1000);
                BigDecimal difAmount = penaltyAmoutIn.add(derateAmount).add(amount).subtract(simplyAmout.multiply(new BigDecimal(day))).subtract(br.getAmount());
                //实际还款+减免金额与应还金额不匹配
                if (difAmount.compareTo(BigDecimal.ZERO) != 0) {
                    throw new ManageException(ManageExceptionEnum.MANAGE_REPAY_REMISSION_AMOUNT_ERROR_CODE_VALUE);
                }
                param.put("penaltyDay", String.valueOf(day));
                param.put("overduePenaltyAmout", simplyAmout.multiply(new BigDecimal(day)));
            }
        }
     /*更新还款信息*/
        if (updateBorrowReplayNew(br, repayTime, param, state) < 0) {
            throw new ManageException(ManageExceptionEnum.MANAGE_REPAY_UPDATE_REPAY_ERROR_CODE_VALUE);
        }
        /*更新借款表和借款进度状态*/
        if (updateBorrow(br.getBorrowId(), br.getUserId(), state) < 0) {
            throw new ManageException(ManageExceptionEnum.MANAGE_REPAY_UPDATE_PROGRESS_ERROR_CODE_VALUE);
        }
        handleAllFinishByBorrowId(br.getBorrowId(), br.getUserId());
        Borrow borrow = borrowMapper.selectByPrimaryKey(br.getBorrowId());
        /*信用额度修改*/
        CreditExample example = new CreditExample();
        example.createCriteria().andConsumerNoEqualTo(br.getUserId()+"");
        List<Credit> credits = creditMapper.selectByExample(example);
        //用户信用额度信息不存在
        if (credits.size() == 0) {
            throw new ManageException(ManageExceptionEnum.MANAGE_REPAY_NO_CREDIT_CODE_VALUE);
        }
        Credit credit = credits.get(0);
        credit.setUnuse(credit.getUnuse().add(borrow.getAmount()));
        credit.setUsed(credit.getUsed().compareTo(borrow.getAmount()) == 1 ? credit.getUsed().subtract(borrow.getAmount()) : BigDecimal.ZERO);
        creditMapper.updateByPrimaryKeySelective(credit);
        // 更新催收订单中的状态
        UrgeRepayOrderExample example1 = new UrgeRepayOrderExample();
        example1.createCriteria().andBorrowIdEqualTo(br.getBorrowId());
        List<UrgeRepayOrder> orders = urgeRepayOrderMapper.selectByExample(example1);
        if (orders.size() > 0) {
            UrgeRepayOrder order = orders.get(0);
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
            Date repayPlanTime = DateUtil.valueOf(time.format(br.getRepayTime()));
            Date repay_time = DateUtil.valueOf(time.format(repayTime));
            // 实际还款时间在应还款时间之前或当天（不对比时分秒），删除催收记录
            if (!repay_time.after(repayPlanTime)) {
                UrgeRepayLogExample example2 = new UrgeRepayLogExample();
                example2.createCriteria().andDueIdEqualTo(order.getId());
                urgeRepayLogMapper.deleteByExample(example2);
                urgeRepayOrderMapper.deleteByExample(example1);
            } else {
                logger.debug("更新存在的催收订单中的状态");
                UrgeRepayLog orderLog = new UrgeRepayLog();
                orderLog.setRemark("用户还款成功");
                orderLog.setWay("10");
                orderLog.setCreateTime(DateUtil.getNow());
                orderLog.setState(UrgeRepayOrderModel.STATE_ORDER_SUCCESS);
                orderLog.setDueId(order.getId());
                orderLog.setBorrowId(order.getBorrowId());
                orderLog.setUserId(order.getUserId());
                //更新催收记录
                urgeRepayLogMapper.insertSelective(orderLog);
                order.setCount(order.getCount() + 1);
                order.setState(orderLog.getState());
                urgeRepayOrderMapper.updateByPrimaryKeySelective(order);
                if (order.getState().equals(UrgeRepayOrderModel.STATE_ORDER_BAD)) {
                    //更新借款状态
                    Borrow b = borrowMapper.selectByPrimaryKey(order.getBorrowId());
                    Borrow borrow1 = new Borrow();
                    borrow1.setId(id);
                    borrow1.setState(BorrowModel.STATE_BAD);
                    borrowMapper.updateByPrimaryKeySelective(borrow1);
                    //添加借款进度
                    BorrowProgress bp = new BorrowProgress();
                    bp.setBorrowId(b.getId());
                    bp.setUserId(b.getUserId());
                    bp.setRemark(BorrowModel.convertBorrowRemark(BorrowModel.STATE_BAD));
                    bp.setState(BorrowModel.STATE_BAD);
                    bp.setCreateTime(new Date());
                    borrowProgressMapper.insertSelective(bp);
                }
            }
        }
        //发送短信还款短信
        smsService.activePayment(br.getUserId(),borrow.getBorrowMainId(),repayTime,amount,borrow.getOrderNo());
        //还款消息通知
        noticesService.activePayment(br.getUserId(),borrow.getBorrowMainId(),repayTime,amount);
        //封存用户信息
        userDataPackageDomain.packageSave(br.getUserId());
    }
    /**
     * <p>所有分期订单都还款完成后的后续处理</p>
     * @param borrowId
     * @param userId
     * */
    private void handleAllFinishByBorrowId(Long borrowId,Long userId){
        Borrow borrow=borrowMapper.selectByPrimaryKey(borrowId);
        handleAllFinishByBorrowMainId(borrow.getBorrowMainId(),borrowId,userId);
    }
    private Boolean isAllFinished(Long mainId) {
        Map<String, Object> map = new HashMap<>();
        map.put("borrowMainId", mainId);
        List<Borrow> borrowList = borrowMapper.listSelective(map);
        Boolean isAllFinish = true;
        for (Borrow temp : borrowList) {
            String borrowState = temp.getState();
            if (!BorrowModel.STATE_FINISH.equals(borrowState) && !BorrowModel.STATE_REMISSION_FINISH.equals
                    (borrowState)) {
                isAllFinish = false;
                break;
            }
        }
        return isAllFinish;
    }
    /**
     * <p>所有分期订单都还款完成后的后续处理</p>
     * @param borrowMainId
     * @param borrowId
     * @param userId
     * */
    private void handleAllFinishByBorrowMainId(Long borrowMainId,Long borrowId,Long userId){
        // 因为cl_borrow_main表是后期添加的，所以cl_borrow表中之前的数据，没有main_id,这些数据只有单期，且id与main表中的id相同，所以直接用id操作 @author yecy 20171225
        Long mainId = borrowMainId;
        // 此处如果所有子账单全部还款完成，则需要修改主账单的状态为40
        boolean isAllFinish;
        if (mainId != null && !mainId.equals(0L)) {
            isAllFinish = isAllFinished(mainId);
        } else {
            mainId = borrowId;
            isAllFinish = true;
        }
        if (isAllFinish) {
            BorrowMain borrowMain = new BorrowMain();
            borrowMain.setId(mainId);
            borrowMain.setState(BorrowModel.STATE_FINISH);
            int msg=borrowMainMapper.updateByPrimaryKeySelective(borrowMain);
            if (msg < 1) {
                throw new BussinessException("未找到cl_borrow_main表中id为" + mainId + "的记录");
            }
            BorrowMain borrowMain1 = borrowMainMapper.selectByPrimaryKey(mainId);
            //borrowMainProcess保存成功的记录
            BorrowMainProgress borrowProgress = new BorrowMainProgress();
            borrowProgress.setBorrowId(borrowMain.getId());
            borrowProgress.setUserId(borrowMain.getUserId());
            if ( BorrowRepayEnum.REPAY_BORROW_STATE.STATE_FINISH.EnumKey().equals(BorrowModel.STATE_PRE)) {
                borrowProgress.setRemark("Borrowing ₹"
                        + StringUtil.isNull(borrowMain.getAmount())
                        + ",Tenure of lending "
                        + borrowMain.getTimeLimit()
                        + " days,Comprehensive cost ₹"
                        + StringUtil.isNull(borrowMain.getFee()));
            } else {
                borrowProgress.setRemark(BorrowModel.convertBorrowRemark(BorrowRepayEnum.REPAY_BORROW_STATE.STATE_FINISH.EnumKey()));
            }
            borrowProgress.setState(BorrowRepayEnum.REPAY_BORROW_STATE.STATE_FINISH.EnumKey());
            borrowProgress.setCreateTime(DateUtil.getNow());
            borrowMainProgressMapper.insertSelective(borrowProgress);
//            //用户额度提升
//            int count = creditsUpgradeService.handleCreditsUpgrade(userId, ZHIMA_FEN.Zhimafen_620);
//            logger.info("*************************>usreId={}的用户额度提升业务逻辑处理结果，处理结果:{}.", new Object[]{userId, count > 0});
        }
    }

    /**
     * 更新借款表和借款进度状态
     *
     * @param borrowId
     * @param userId
     * @param state
     * @return
     */
    public int updateBorrow(long borrowId, long userId, String state) {
        int i = 0;
        // 更新借款状态
        Borrow borrow = new Borrow();
        borrow.setId(borrowId);
        borrow.setState(state);
        i = borrowMapper.updateByPrimaryKeySelective(borrow);
        if (i > 0) {
            // 添加借款进度
            BorrowProgress bp = new BorrowProgress();
            bp.setBorrowId(borrowId);
            bp.setUserId(userId);
            bp.setRemark(BorrowModel.convertBorrowRemark(state));
            bp.setState(state);
            bp.setCreateTime(DateUtil.getNow());
            return borrowProgressMapper.insertSelective(bp);
        }
        return i;
    }

    /**
     * 更新还款计划和还款记录表(确认还款用)
     *
     * @param br
     * @param repayTime
     * @param param
     * @return
     */
    public int updateBorrowReplayNew(BorrowRepay br, Date repayTime,
                                     Map<String, Object> param, String state) {
        // 更新还款计划状态
        int i = 0;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", br.getId());
        paramMap.put("state", BorrowRepayModel.STATE_REPAY_YES);
        if (BorrowModel.STATE_REMISSION_FINISH.equals(state)) {
            paramMap.put("penaltyAmout", param.get("overduePenaltyAmout"));
        } else {
            paramMap.put("penaltyAmout", param.get("penaltyAmout"));
        }
        paramMap.put("penaltyDay", param.get("penaltyDay"));


        i = borrowRepayMapper.updateParam(paramMap);
        if (i > 0) {
            // 生成还款记录
            BorrowRepayLog log = new BorrowRepayLog();
            log.setBorrowId(br.getBorrowId());
            log.setRepayId(br.getId());
            log.setUserId(br.getUserId());
            log.setAmount(new BigDecimal((Double)param.get("amount")));// 实际还款金额
            log.setRepayTime(repayTime);// 实际还款时间
            log.setPenaltyDay(Integer.parseInt((String) param.get("penaltyDay")));
            log.setPenaltyAmout(new BigDecimal((Double) param.get("penaltyAmout")));
            //计算提前还款手续费
            BigDecimal fee=getFee(br.getBorrowId(),br.getUserId(), br.getRepayTime(), repayTime);
            log.setFee(fee);
            log.setSerialNumber((String) param.get("serialNumber"));
            log.setRepayAccount((String) param.get("repayAccount"));
            log.setRepayWay((String) param.get("repayWay"));
            log.setCreateTime(DateUtil.getNow());
            log.setConfirmId((Long) param.get("confirmId"));
            log.setConfirmTime((Date) param.get("confirmTime"));
            return borrowRepayLogMapper.insertSelective(log);
        }
        return i;
    }

    /**
     * <p>根据给定参数求提前手续费</p>
     * @param borrowId 子订单id
     * @param userId   用户id
     * @param repayTime
     * @param reallyTime
     * @return 手续费
     * */
    protected BigDecimal getFee(Long borrowId,Long userId,Date repayTime,Date reallyTime){
        BorrowMain borrowMain=borrowMainMapper.getBowMainByBorrowIdWithUserId(borrowId, userId);
        return getFee(borrowMain.getAmount(), borrowMain.getTemplateInfo(), repayTime, reallyTime);
    }

    /**
     * <p>根据给定参数求提前手续费</p>
     * @param borrowAmount 借款金额
     * @param templateInfoJSON 借款模板JSON串
     * @param repayTime
     * @param reallyTime
     * @return 手续费
     * */
    protected BigDecimal getFee(BigDecimal borrowAmount, String templateInfoJSON,Date repayTime,Date reallyTime){
        TemplateInfoModel templateInfo= new TemplateInfoModel().parseTemplateInfo(borrowAmount, templateInfoJSON);
        return StaginRepaymentPlanData.getPrepayment(repayTime, reallyTime, templateInfo.getInterest(),
                templateInfo.getCycle());
    }

    @Override
    public List<List<String>> fileBatchRepay(MultipartFile repayFile, String type, Long userId) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("repayState", 20);
        List<BorrowRepayModel> list = borrowRepayMapper.listModelNew(params);
        String ext = repayFile.getOriginalFilename().substring(repayFile.getOriginalFilename().lastIndexOf("."));
        if (!(".xlsx".equals(ext) || ".xls".equals(ext))) {
            throw new ManageException(ManageExceptionEnum.MANAGE_REPAY_FILE_TYPE_ERROR_CODE_VALUE);
        }
        String title = "批量还款匹配结果";
        List<List<String>> result = new ArrayList<List<String>>();
        if (type.equals("alpay")) {
            result = parserByFile(repayFile, list,userId);
        } else if ("bank".equals(type)) {
            result = toPaseBank(repayFile, list,userId);
            RepayExcelModel.createWorkBook(result, title);
        } else {
            throw new ManageException(ManageExceptionEnum.MANAGE_REPAY_FAIL_FILE_TYPE_CODE_VALUE);
        }
        return result;
    }

    @Override
    public BorrowRepay getById(Long id) {
        return borrowRepayMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<BorrowModel> listModelNotUrge(Map<String, Object> params, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        return (Page<BorrowModel>) borrowRepayMapper.listModelNotUrge(params);
    }

    @Override
    public void genRepayPlans(List<Borrow> borrowList, BorrowMain borrowMain) {
        genRepayPlans(borrowList, borrowMain, false);
    }
    @Override
    public void genRepayPlans(List<Borrow> borrowList, BorrowMain borrowMain, Boolean isMockData) {
        if (CollectionUtils.isEmpty(borrowList)) {
            return;
        }
        List<BorrowRepay> repayList = new ArrayList<>(borrowList.size());
        for (Borrow borrow : borrowList) {
            BorrowRepay br = new BorrowRepay();
            BigDecimal amount =borrow.getAmount().add(borrow.getFee());
            br.setAmount(amount);
            br.setBorrowId(borrow.getId());
            br.setUserId(borrow.getUserId());
            String repay = DateUtil.dateStr2(DateUtil.rollDay(borrow.getCreateTime(), Integer.parseInt(borrow
                    .getTimeLimit()) - 1));
            br.setRepayTime(DateUtil.valueOf(repay + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
            br.setState(BorrowRepayModel.STATE_REPAY_NO);
            br.setPenaltyAmout(new BigDecimal(0.0));
            br.setPenaltyDay(0);
            repayList.add(br);
        }
        borrowRepayMapper.saveAll(repayList);
//        if (!"dev".equals(Global.getValue("app_environment")) && !isMockData) {
//            authApply(repayList, borrowMain);
//        }
    }

    @Override
    public List listExport(Map<String, Object> params) {
        params.put("totalCount", ExportConstant.TOTAL_LIMIT);
        List<BorrowRepayModel> list = borrowRepayMapper.listModel(params);
        for (BorrowRepayModel mbm : list) {
            mbm.setState(ExportConstant.change(mbm.getState()));
        }
        return list;
    }

    @Override
    public List<BorrowRepay> listSelective(Map<String, Object> paramMap) {
        return borrowRepayMapper.listSelective(paramMap);
    }

    @Override
    public int updateLate(BorrowRepay br) {
        return borrowRepayMapper.updateByPrimaryKeySelective(br);
    }

    @Override
    public List<OverDueUser> listOverDueUser() {
        return borrowRepayMapper.listOverDueUser();
    }

    /**
     * 解析支付宝财务明显账单,与备注信息有匹配信息确认还款
     *
     * @param repayFile
     * @param list
     * @param userId
     * @return
     * @throws Exception
     */
    public List<List<String>> parserByFile(MultipartFile repayFile, List<BorrowRepayModel> list, Long userId) throws Exception {
        //csv文档格式解析
        //CsvParser parser=new CsvParser("");
        //arr=parser.getParsedArrayList(repayFile);
        //xls文档格式解析
        ReadExcelUtils excelReader = new ReadExcelUtils(repayFile);
        List<List<String>> arr = excelReader.readExcelContent();
        //logger.info("获得支付宝账单表格的内容:"+JSONObject.toJSONString(arr));
        int j = 0;
        List<AlipayModel> alipayList = new ArrayList<AlipayModel>();
        AlipayModel model = null;
        for (int i = 0; i < arr.size(); i++) {
            model = new AlipayModel();
            List<String> ls = arr.get(i);
            for (int k = 0; k < ls.size(); k++) {
                String item = ls.get(k);
                if ("".equals(item)) {
                    continue;
                }
                if (item.contains("账务流水号")) {
                    j = i;
                    arr.get(i).add(ls.size(), "是否有备注信息与还款计划匹配");
                }
            }
            if (j != 0 && j + 1 < ls.size() && j + 1 <= i) {
                model.setSerialNumber(ls.get(1));
                model.setAccount(ls.get(5));
                model.setAmount(ls.get(6));
                model.setRemark(ls.get(11));
                model.setRepayTime(ls.get(4) != null ? DateUtil.valueOf(ls.get(4), "yyyy/MM/dd HH:mm") : null);
                if (model != null && model.getAccount() != null) {
                    alipayList.add(model);
                }
                boolean flag = false;
                flag = remarkPay(flag, model, list,userId);
                arr.get(i).add(ls.size(), flag ? "有" : "无");
            }
        }
        if (alipayList.size() <= 0) {
            throw new BussinessException("没有解析到匹配的数据，请上传正确的文档");
        }
         logger.info("=获得支付宝账单表格的内容==="+ JSONObject.toJSONString(arr));
        return arr;
    }
    public List<List<String>> toPaseBank(MultipartFile file, List<BorrowRepayModel> list, Long userId) throws Exception {
        ReadExcelUtils excelReader = new ReadExcelUtils(file);
        List<List<String>> arr = excelReader.readExcelContent();
        logger.info("获得银行卡帐单内容:" + JSONObject.toJSONString(arr));
        List<AlipayModel> alipayList = new ArrayList<AlipayModel>();
        AlipayModel model = null;
        int j = 0;
        for (int i = 0; i < arr.size(); i++) {
            model = new AlipayModel();
            List<String> ls = arr.get(i);
            for (int k = 0; k < ls.size(); k++) {
                String item = ls.get(k);
                if ("".equals(item)) {
                    continue;
                }
                if (item.contains("交易日")) {
                    j = i;
                    arr.get(i).add(ls.size(), "是否有备注信息与还款计划匹配");
                }
            }
            if (j != 0 && j + 1 < ls.size() && j + 1 <= i) {
                String repayTime = ls.get(0) + ls.get(1);
                model.setRepayTime(repayTime != "" ? DateUtil.valueOf(repayTime, DateUtil.DATEFORMAT_STR_011) : null);
                model.setSerialNumber(ls.get(8));
                model.setAccount(ls.get(17));
                model.setAmount(ls.get(6));
                model.setRemark(ls.get(7));
                if (model != null && model.getAccount() != null) {
                    alipayList.add(model);
                }
                boolean flag = false;
                flag = remarkPay(flag, model, list, userId);
                arr.get(i).add(ls.size(), flag ? "有" : "无");
            }
        }
        if (alipayList.size() <= 0) {
            throw new BussinessException("没有解析到匹配的数据，请上传正确的文档");
        }
        logger.info("==对比之后的值==:"+JSONObject.toJSONString(arr));
        return arr;
    }
    /**
     * 对账文档解析结果与还款计划对比
     *
     * @param flag
     * @param model
     * @param list
     * @param userId
     * @return
     */
    public boolean remarkPay(boolean flag, AlipayModel model, List<BorrowRepayModel> list, Long userId) throws Exception {
        for (BorrowRepayModel repay : list) {
            // 账单中存在未还款的用户信息   只匹配手机号码
            if (model.getRemark().contains(repay.getPhone())) {
                flag = true;
                logger.info("批量还款匹配到的还款数据==" + repay.getId() + "=" + model.getRemark() + "=" + repay.getPhone() + "=" + repay.getRealName());
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("id", repay.getId());
                param.put("repayTime", model.getRepayTime());
                param.put("repayWay", BorrowRepayEnum.REPAY_WAY.REPAY_WAY_ALIPAY_TRANSFER.getCode());
                param.put("repayAccount", model.getAccount());
                param.put("penaltyAmout", 0.0);
                if (Double.valueOf(model.getAmount()) < Double.valueOf(repay.getRepayAmount())) {
                    param.put("state", BorrowRepayModel.OVERDUE_RELIEF);
                } else {
                    param.put("state", BorrowRepayModel.NORMAL_REPAYMENT);
                }
                param.put("serialNumber", model.getSerialNumber());
                param.put("amount", Double.parseDouble(model.getAmount()));
                param.put("confirmTime",new Date());
                param.put("confirmId",userId);
                confirmRepayNew(param);
                break;
            }
        }
        return flag;
    }
}
