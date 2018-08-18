package com.adpanshi.cashloan.manage.cl.model.expand;

import com.adpanshi.cashloan.manage.cl.enums.BorrowEnum;
import com.adpanshi.cashloan.manage.cl.model.Borrow;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Vic Tang
 * @Description: 订单扩展类
* @date 2018/8/1 21:50
 */
public class BorrowModel extends Borrow {

    private static final long serialVersionUID = 1L;

    public static BorrowModel instance(Borrow borrow) {
        BorrowModel borrowModel = new BorrowModel();
        BeanUtils.copyProperties(borrow, borrowModel);
        return borrowModel;
    }

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 状态中文含义
     */
    private String stateStr;

    /**
     * 待还款金额（本金+管理费+逾期费用）
     */
    private BigDecimal repayAmount;

    /**
     * 还款时间
     */
    private String repayTime;

    /**
     * 逾期罚金
     */
    private BigDecimal penaltyAmout;

    /**
     * 逾期天数
     */
    private String penaltyDay;

    /**
     * 借款订单id
     */
    private long borrowId;
    /**
     * 放款时间
     */
    private Date loanTime;

    /**
     * 逾期等级
     */
    private String level;

    /**
     * 是否黑名单 ，10是20不是(对应cl_user_base_info中的state)
     */
    private String blackState;

    /**
     *  拉黑原因
     */
    private String blackReason;

    /**
     *  实际还款金额
     */
    private BigDecimal realRepaymentAmout;

    /**
     * 复审人姓名
     * @return
     */
    private  String auditName;/*zy*/

    private String auditData;

    /**
     * 用户注册端
     * @return
     */
    private String registerClient;

    /**
     * 用户渠道名称
     * @return
     */
    private String channelName;

    /**
     * 用户渠道code
     * @return
     */
    private String channelCode;

    /**
     * 订单生成时间-起始时间
     * */
    private String startCreateTime;

    /**
     * 订单生成时间-结束时间
     * */
    private String endCreateTime;
    /**
     * 拒绝原因
     */
    private String orderView;
    /**
     * 订单分配人
     */
    private String sysUserName;

    /**
     * 期数
     * */
    private Integer phase;

    public Date getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }

    public BigDecimal getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }

    public BigDecimal getPenaltyAmout() {
        return penaltyAmout;
    }

    public void setPenaltyAmout(BigDecimal penaltyAmout) {
        this.penaltyAmout = penaltyAmout;
    }

    public String getPenaltyDay() {
        return penaltyDay;
    }

    public void setPenaltyDay(String penaltyDay) {
        this.penaltyDay = penaltyDay;
    }

    public String getStateStr() {
        this.stateStr = BorrowEnum.ALL_BORROW_STATE.getByEnumKey(this.getState()).EnumValue();
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取借款订单id
     * @return borrowId
     */
    public long getBorrowId() {
        return borrowId;
    }

    /**
     * 设置借款订单id
     * @param borrowId
     */
    public void setBorrowId(long borrowId) {
        this.borrowId = borrowId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getBlackReason() {
        return blackReason;
    }

    public void setBlackReason(String blackReason) {
        this.blackReason = blackReason;
    }

    public String getBlackState() {
        return blackState;
    }

    public void setBlackState(String blackState) {
        this.blackState = blackState;
    }

    public BigDecimal getRealRepaymentAmout() {
        return realRepaymentAmout;
    }

    public void setRealRepaymentAmout(BigDecimal realRepaymentAmout) {
        this.realRepaymentAmout = realRepaymentAmout;
    }

    public String getAuditName() {return auditName;}/*zy 2017.7.21*/

    public void setAuditName(String auditName) {this.auditName = auditName;}

    public String getAuditData() {
        return auditData;
    }

    public void setAuditData(String auditData) {
        this.auditData = auditData;
    }

    public String getRegisterClient() {
        return registerClient;
    }

    public void setRegisterClient(String registerClient) {
        this.registerClient = registerClient;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(String startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public String getOrderView() {
        return orderView;
    }

    public void setOrderView(String orderView) {
        this.orderView = orderView;
    }
    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public Integer getPhase() {
        return phase;
    }

    public void setPhase(Integer phase) {
        this.phase = phase;
    }
    /**
     * 申请成功待审核
     */
    public static final String STATE_PRE = "10";
    /**
     * 自动审核通过
     */
    public static final String STATE_AUTO_PASS = "20";
    /**
     * 自动审核不通过
     */
    public static final String STATE_AUTO_REFUSED = "21";
    /**
     * 自动审核未决待人工复审
     */
    public static final String STATE_NEED_REVIEW = "22";
    /**
     * 人工复审挂起--订单辅助状态
     */
    public static final String STATE_HANGUP = "25";
    /**
     * 人工复审通过
     */
    public static final String STATE_PASS = "26";
    /**
     * 人工复审不通过
     */
    public static final String STATE_REFUSED = "27";
    /**
     * 放款成功
     */
    public static final String STATE_REPAY = "30";
    /**
     * 放款失败
     */
    public static final String STATE_REPAY_FAIL = "31";
    /**
     * 还款成功
     */
    public static final String STATE_FINISH = "40";
    /**
     * 还款成功-金额减免
     */
    public static final String STATE_REMISSION_FINISH = "41";
    /**
     * 逾期
     */
    public static final String STATE_DELAY = "50";
    /**
     * 坏账
     */
    public static final String STATE_BAD = "90";

//    /**
//     * 临时状态-自动审核
//     */
//    public static final String STATE_TEMPORARY_AUTO_PASS = "12";   //70-初审审核成功 72初审待人工复审  初审审核成功，待活体（自动）
//
//    /**
//     * 临时状态-自动审核未决待人工复审
//     */
//    public static final String STATE_TEMPORARY_NEED_REVIEW = "14";   //70-初审审核成功 72初审待人工复审 初审审核成功，待活体（人工）
//
//    /**
//     * 借款订单发送给银程，等待银程同意 20171124 @author yecy
//     */
//    public static final String STATE_WAIT_AGREE = "28";
//    /**
//     * 银程匹配成功，等待连连放款 20171124 @author yecy
//     */
//    public static final String STATE_WAIT_REPAY = "29";
    /**
     * 响应给管理后台的借款/借款进度状态中文描述转换
     *
     * @return stateStr
     */
    public static String manageConvertBorrowState(String state) {
        String stateStr = " - ";
        if (STATE_PRE.equals(state)) {
            stateStr = ("申请成功待审核");
        } else if (STATE_AUTO_PASS.equals(state)) {
            stateStr = ("自动审核通过");
        } else if (STATE_AUTO_REFUSED.equals(state)) {
            stateStr = ("自动审核不通过 ");
        } else if (STATE_NEED_REVIEW.equals(state)) {
            stateStr = ("待人工复审");
        } else if (STATE_PASS.equals(state)) {
            stateStr = ("人工复审通过");
        } else if (STATE_REFUSED.equals(state)) {
            stateStr = ("人工复审不通过");
//        } else if (STATE_TEMPORARY_AUTO_PASS.equals(state)) {//对应 自动审核通过
//            stateStr = ("初审通过");
//        } else if (STATE_TEMPORARY_NEED_REVIEW.equals(state)) {//对应  待人工复审
//            stateStr = ("初审待复审");
        } else if (STATE_REPAY.equals(state)) {
            stateStr = ("放款成功");
        } else if (STATE_REPAY_FAIL.equals(state)) {
            stateStr = ("放款失败");
        } else if (STATE_FINISH.equals(state)) {
            stateStr = ("还款成功");
        } else if (STATE_REMISSION_FINISH.equals(state)) {
            stateStr = ("还款成功-金额减免");
        } else if (STATE_DELAY.equals(state)) {
            stateStr = ("已逾期");
        } else if (STATE_BAD.equals(state)) {
            stateStr = ("已坏账");
        } else if (STATE_HANGUP.equals(state)) {
            stateStr = ("人工审核挂起");
//        } else if (STATE_WAIT_AGREE.equals(state)) { //20171124 @author yecy
//            stateStr = "等待审批";
//        } else if (STATE_WAIT_REPAY.equals(state)) { //20171124 @author yecy
//            stateStr = "等待放款";
        }

        return stateStr;
    }

    /**
     * 响应给app的借款状态中文描述转换
     *
     * @param state
     * @return
     */
    public static String apiConvertBorrowState(String state) {
        String stateStr = state;
        if (BorrowModel.STATE_PRE.equals(state)) {
            stateStr = "待审核";
        } else if (BorrowModel.STATE_AUTO_PASS.equals(state)) {
            stateStr = "审核通过";
        } else if (BorrowModel.STATE_AUTO_REFUSED.equals(state)) {
            stateStr = "审核不通过";
        } else if (BorrowModel.STATE_NEED_REVIEW.equals(state)) {
            stateStr = "待审核";
        } else if (BorrowModel.STATE_PASS.equals(state)) {
            stateStr = "审核通过";
        } else if (BorrowModel.STATE_REFUSED.equals(state)) {
            stateStr = "审核不通过";
//        } else if (BorrowModel.STATE_TEMPORARY_AUTO_PASS.equals(state)) {//对应 自动审核通过
//            stateStr = "审核通过";
//        } else if (BorrowModel.STATE_TEMPORARY_NEED_REVIEW.equals(state)) {//对应  待人工复审
//            stateStr = "待审核";
        } else if (BorrowModel.STATE_REPAY.equals(state)) {
            stateStr = "还款中";
        } else if (BorrowModel.STATE_REPAY_FAIL.equals(state)) {
            stateStr = "放款失败";
        } else if (BorrowModel.STATE_FINISH.equals(state)) {
            stateStr = "已还款";
        } else if (BorrowModel.STATE_REMISSION_FINISH.equals(state)) {
            stateStr = "已还款";
        } else if (BorrowModel.STATE_DELAY.equals(state)) {
            stateStr = "已逾期";
        } else if (BorrowModel.STATE_BAD.equals(state)) {
            stateStr = "已逾期";
//        } else if (STATE_WAIT_AGREE.equals(state)) { //20171124 @author yecy
//            stateStr = "审批中";
//        } else if (STATE_WAIT_REPAY.equals(state)) { //20171124 @author yecy
//            stateStr = "放款中";
        }
        return stateStr;
    }

    /**
     * 借款状态中文描述转换
     *
     * @param state
     * @return
     */
    public static String convertBorrowRemark(String state) {
        String remarkStr = " - ";
        if (BorrowModel.STATE_PRE.equals(state)) {
            remarkStr = "Please wait patiently during the data review";
        } else if (BorrowModel.STATE_AUTO_PASS.equals(state)) {
            remarkStr = "Congratulations on the loan approval";
        } else if (BorrowModel.STATE_AUTO_REFUSED.equals(state)) {
            remarkStr = "Failed to pass the audit";
        } else if (BorrowModel.STATE_NEED_REVIEW.equals(state)) {
            remarkStr = "Please wait patiently during the data review";
        } else if (BorrowModel.STATE_PASS.equals(state)) {
            remarkStr = "Congratulations on the loan approval";
//        } else if (BorrowModel.STATE_TEMPORARY_AUTO_PASS.equals(state)) {//对应 自动审核通过
//            remarkStr = "恭喜通过初步审核";
//        } else if (BorrowModel.STATE_TEMPORARY_NEED_REVIEW.equals(state)) {//对应  待人工复审
//            remarkStr = "系统核实中,请耐心等待";
        } else if (BorrowModel.STATE_REFUSED.equals(state)) {
            remarkStr = "Failed to pass the audit";
        } else if (BorrowModel.STATE_REPAY.equals(state)) {
            remarkStr = "Your applied loan has been made successfully, Please check your bank card bill";
        } else if (BorrowModel.STATE_REPAY_FAIL.equals(state)) {
            remarkStr = "放款失败";
        } else if (BorrowModel.STATE_FINISH.equals(state)) {
            remarkStr = "The repayment is successful";
        } else if (BorrowModel.STATE_REMISSION_FINISH.equals(state)) {
            remarkStr = "The borrower cannot pay the full amount of the loan and the repayment is successful";
        } else if (BorrowModel.STATE_DELAY.equals(state)) {
            remarkStr = "您的借款已逾期";
        } else if (BorrowModel.STATE_BAD.equals(state)) {
            remarkStr = "经长时间催收,没有结果";
//        } else if (STATE_WAIT_AGREE.equals(state)) { //20171124 @author yecy
//            remarkStr = "恭喜通过审核，等待审批中";
//        } else if (STATE_WAIT_REPAY.equals(state)) { //20171124 @author yecy
//            remarkStr = "恭喜通过审批，等待放款中";
        }
        return remarkStr;
    }
}
