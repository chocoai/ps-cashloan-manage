package com.adpanshi.cashloan.manage.controller;

import com.adpanshi.cashloan.common.enums.OrderPrefixEnum;
import com.adpanshi.cashloan.manage.arc.service.CreditService;
import com.adpanshi.cashloan.manage.cl.enums.BorrowEnum;
import com.adpanshi.cashloan.manage.cl.model.*;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowModel;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowTemplateModel;
import com.adpanshi.cashloan.manage.cl.model.expand.PayLogModel;
import com.adpanshi.cashloan.manage.cl.service.*;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.pojo.ResultModel;
import com.alibaba.fastjson.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tool.util.BigDecimalUtil;
import tool.util.DateUtil;
import tool.util.StringUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 支付记录Controller
 *
 * @version 1.0.0
 * @date 2017-03-07 16:18:56
 */
@Scope("prototype")
@Controller
public class ManagePayLogController extends ManageBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ManagePayLogController.class);

    @Resource
    private PayLogService payLogService;
    @Resource
    private BorrowService borrowService;
    @Resource
    private BorrowProgressService borrowProgressService;
    @Resource
    private BorrowRepayService borrowRepayService;
//    @Resource
//    private ProfitAmountService profitAmountService;
    @Resource
    private BorrowMainService borrowMainService;
    @Resource
    private BorrowMainProgressService borrowMainProgressService;
    @Resource
    private CreditService creditService;
    @Resource
    private SmsService clSmsService;
    @Resource
    private NoticesService clNoticesService;

    /**
     * 模拟付款成功 - 异步回调
     * @param orderNo
     * @throws Exception
     * @update: nmnl...授权模拟
     */
    @RequestMapping(value = "/pay/testPayNotify.htm", method = RequestMethod.POST)
    public ResponseEntity<ResultModel> testPaymentNotify(@RequestParam(value = "orderNo") String orderNo) throws Exception {
        PayLog payLog = payLogService.findPayLogByLastOrderNoWithBorrowId(orderNo, null);
        String codeMsg = null;
        if(null  == payLog ){
            logger.warn("未查询到对应的支付订单");
            codeMsg ="未查询到对应的支付订单";
        }
        if (PayLogModel.STATE_PAYMENT_WAIT.equals(payLog.getState()) ||
                PayLogModel.STATE_AUDIT_PASSED.equals(payLog.getState()) ||
                PayLogModel.STATE_PAYMENT_FAILED.equals(payLog.getState())) {
            if(PayLogModel.SCENES_LOANS.equals(payLog.getScenes())){
                // 修改借款状态
                Long mainBorrowId = payLog.getBorrowMainId();
                BorrowMain borrowMain = borrowMainService.getById(mainBorrowId);
                String settleDate = "";
                DateTimeFormatter dtf= DateTimeFormat.forPattern("yyyyMMdd");
                DateTime now = DateTime.now();
                DateTime payTime;
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(settleDate)){
                    payTime = DateTime.parse(settleDate,dtf);
                    payTime = now.withDate(payTime.getYear(),payTime.getMonthOfYear(),payTime.getDayOfMonth());
                }else {
                    payTime = now;
                }
                DateTime repayTime = payTime.plusDays(Integer.parseInt(borrowMain.getTimeLimit())-1);
                repayTime = repayTime.withTime(23,59,59,0);
                borrowMainService.updatePayState(mainBorrowId,BorrowEnum.REFUND_BORROW_STATE.STATE_REPAY.EnumKey(),payTime.toDate(),repayTime.toDate());
                borrowMain.setState(BorrowEnum.REFUND_BORROW_STATE.STATE_REPAY.EnumKey());
                // 放款进度添加
                BorrowMainProgress bp = new BorrowMainProgress();
                bp.setUserId(payLog.getUserId());
                bp.setBorrowId(payLog.getBorrowMainId());
                bp.setState(BorrowEnum.REFUND_BORROW_STATE.STATE_REPAY.EnumKey());
                bp.setRemark(BorrowModel.convertBorrowRemark(bp.getState()));
                bp.setCreateTime(DateUtil.getNow());
                borrowMainProgressService.insert(bp);
                String templateInfo = borrowMain.getTemplateInfo();
                BorrowTemplateModel template = JSONObject.parseObject(templateInfo, BorrowTemplateModel.class);
                Long cycle = Long.parseLong(template.getCycle());
                Long timeLimit = Long.parseLong(template.getTimeLimit());
                Long borrowSize = timeLimit / cycle;
                // 创建借款账单（第一条借款账单创建时间以连连支付时间为准，之后的为前一条创建时间+前一条期限） @author yecy 20171215
                List<Borrow> borrowList = createBorrows(borrowMain,"", borrowSize);
                //根据主借款流程，生成子借款流程
                createBorrowProcessList(borrowList,borrowMain);
                // 根据借款账单生成还款计划并授权(应还时间根据借款账单的创建时间生成)
                borrowRepayService.genRepayPlans(borrowList,borrowMain);
                // 更新订单状态
                PayLog record = new PayLog();
                record.setId(payLog.getId());
                record.setUpdateTime(new Date());
                record.setState(PayLogModel.STATE_PAYMENT_SUCCESS);
                payLogService.updateSelective(record);
                logger.info("***********************************>放款异步回调，开始更新用户信用额度.....");

                //@remarks: 只有真正放款.才会更改额度. @date: 20170724 @author: nmnl
                int TMPcount=creditService.modifyCreditAfterLoan(borrowMain.getUserId(),borrowMain.getAmount());
                logger.info("***********************************>修改用户信用额度结果:{}......",new Object[]{TMPcount>0});
                //发送放款短信
                clSmsService.payment(payLog.getUserId(),borrowMain.getId(),new Date(),borrowMain.getRealAmount(),orderNo);
                //放款消息通知
                clNoticesService.payment(payLog.getUserId(),borrowMain.getId(),borrowMain.getRealAmount());
            }
        }else{
            codeMsg = "订单状态错误";
        }
        Map<String, Object> result = new HashMap<String, Object>();
        if (StringUtil.isBlank(codeMsg)) {
            result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, Constant.OPERATION_SUCCESS);
        } else {
            result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
            result.put(Constant.RESPONSE_CODE_MSG, codeMsg);
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    private List<Borrow> createBorrows(BorrowMain borrowMain, String settleDate, Long borrowSize) {
        List<Borrow> borrowList = new ArrayList<>(borrowSize.intValue());
        Date payTime;
        if (StringUtils.isEmpty(settleDate)){
            payTime = DateUtil.getNow();
        }else {
            payTime = DateUtil.valueOf(settleDate, DateUtil.DATEFORMAT_STR_012);
        }
        Borrow firstBorrow = getFirstBorrow(borrowMain, borrowSize.doubleValue(), payTime);
        borrowList.add(0, firstBorrow);


        BigDecimal otherAmount = borrowMain.getAmount().divide(new BigDecimal(borrowSize));
        BigDecimal otherRealAmount = borrowMain.getRealAmount().divide(new BigDecimal(borrowSize));
        BigDecimal otherFee = borrowMain.getFee().divide(new BigDecimal(borrowSize));
        Long otherTimeLimit = Long.valueOf(borrowMain.getTimeLimit())/borrowSize;

        Date beginTime = DateUtil.rollDay(payTime,Integer.valueOf(firstBorrow.getTimeLimit()));
        for (int i = 1; i < borrowSize; i++) {
            Borrow borrow = new Borrow();
            borrow.setAmount(otherAmount);
            borrow.setRealAmount(otherRealAmount);
            borrow.setFee(otherFee);
            borrow.setTimeLimit(String.valueOf(otherTimeLimit));

            Date createTime =  DateUtil.rollDay(beginTime,(i-1)*otherTimeLimit.intValue());
            borrow.setCreateTime(createTime);
            String orderNo = getOrderNo(borrowMain,i + 1);
            borrow.setOrderNo(orderNo);
            fillBorrow(borrow, borrowMain);
            borrowList.add(borrow);
        }
        borrowService.saveAll(borrowList);
        return borrowList;
    }

    private void createBorrowProcessList(List<Borrow> borrowList, BorrowMain borrowMain) {
        List<BorrowProgress> processList = new ArrayList<>();
        List<BorrowMainProgress> mainProcessList = borrowMainProgressService.getProcessByMainId(borrowMain.getId());
        for (Borrow borrow : borrowList){
            for (BorrowMainProgress mainProcess : mainProcessList){
                BorrowProgress bp = new BorrowProgress();
                BeanUtils.copyProperties(mainProcess,bp,"id","borrowId");
                bp.setBorrowId(borrow.getId());

                processList.add(bp);
            }
        }
        borrowProgressService.saveAll(processList);
    }


    private Borrow getFirstBorrow(BorrowMain borrowMain, Double borrowSize, Date createTime){
        Borrow borrow = new Borrow();

        Double otherSize = BigDecimalUtil.sub(borrowSize,1d);
        BigDecimal otherAmount = borrowMain.getAmount().divide(new BigDecimal(borrowSize));
        BigDecimal otherRealAmount = borrowMain.getRealAmount().divide(new BigDecimal(borrowSize));
        BigDecimal otherFee = borrowMain.getFee().divide(new BigDecimal(borrowSize));
        BigDecimal amount = borrowMain.getAmount().subtract(otherAmount.multiply(new BigDecimal(otherSize)));
        borrow.setAmount(amount);
        BigDecimal realAmount = borrowMain.getRealAmount().subtract(otherRealAmount.multiply(new BigDecimal(otherSize)));
        borrow.setRealAmount(realAmount);
        BigDecimal fee = borrowMain.getFee().subtract(otherFee.multiply(new BigDecimal(otherSize)));
        borrow.setFee(fee);
        Long mainTimeLimit = Long.valueOf(borrowMain.getTimeLimit());
        Long otherTimeLimit = mainTimeLimit/borrowSize.longValue();
        Long timeLimit = mainTimeLimit - otherTimeLimit * otherSize.longValue();
        borrow.setTimeLimit(String.valueOf(timeLimit));
        borrow.setCreateTime(createTime);
        String orderNo = getOrderNo(borrowMain,1);
        borrow.setOrderNo(orderNo);
        fillBorrow(borrow,borrowMain);
        return borrow;
    }

    private String getOrderNo(BorrowMain borrowMain,Integer num){
        StringBuilder orderNo = new StringBuilder();
        orderNo.append(OrderPrefixEnum.REPAYMENT.getCode()).append(borrowMain.getOrderNo().substring(1)).append("X").append(num);
        return orderNo.toString();
    }

    private void fillBorrow(Borrow borrow,BorrowMain borrowMain){
        borrow.setBorrowMainId(borrowMain.getId());
        borrow.setState(borrowMain.getState());
        borrow.setUserId(borrowMain.getUserId());
        borrow.setCardId(borrowMain.getCardId());
        borrow.setClient(borrowMain.getClient());
        borrow.setAddress(borrowMain.getAddress());
        borrow.setCoordinate(borrowMain.getCoordinate());
        borrow.setIp(borrowMain.getIp());
    }
}
