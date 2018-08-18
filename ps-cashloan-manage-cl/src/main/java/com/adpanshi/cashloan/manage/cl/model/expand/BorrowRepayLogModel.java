package com.adpanshi.cashloan.manage.cl.model.expand;

import com.adpanshi.cashloan.manage.cl.model.BorrowRepayLog;

import java.util.Date;

/**
 * @author Vic Tang
 * @Description: 还款记录扩展类
 * @date 2018/8/1 10:37
 */
public class BorrowRepayLogModel extends BorrowRepayLog {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 状态
     */
    private String state;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 借款金额
     */
    private Double borrowAmount;
    /**
     * 还款金额（还款计划）
     */
    private Double repayAmount;
    /**
     * 已还款金额（还款记录）
     */
    private Double repayLogAmount;
    /**
     * 实际还款时间 zy

     */
    private String repayTimeStr;
    /**
     * 应还款时间
     */
    private String repayPlanTimeStr;
    /**
     * 应还逾期金额
     */
    private Double repayPenalty;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 手机号
     */
    private String phone;

    /**借款期限*/
    private String timeLimit;

    /**
     * 身份证号
     */
    private String idNo;

    public Date getRepayPlanTime() {
        return repayPlanTime;
    }

    public void setRepayPlanTime(Date repayPlanTime) {
        this.repayPlanTime = repayPlanTime;
    }

    private Date repayPlanTime;

    public Double getRepayPenalty() {
        return repayPenalty;
    }

    public void setRepayPenalty(Double repayPenalty) {
        this.repayPenalty = repayPenalty;
    }

    public String getRepayPlanTimeStr() {
        return repayPlanTimeStr;
    }

    public void setRepayPlanTimeStr(String repayPlanTimeStr) {
        this.repayPlanTimeStr = repayPlanTimeStr;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getBorrowAmount() {
        return borrowAmount;
    }

    public void setBorrowAmount(Double borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    public Double getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(Double repayAmount) {
        this.repayAmount = repayAmount;
    }

    public Double getRepayLogAmount() {
        return repayLogAmount;
    }

    public void setRepayLogAmount(Double repayLogAmount) {
        this.repayLogAmount = repayLogAmount;
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

    //zy 2017.7.31
    public String getRepayTimeStr() {
        return repayTimeStr;
    }
    public void setRepayTimeStr(String repayTimeStr) {
        this.repayTimeStr = repayTimeStr;;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
}
