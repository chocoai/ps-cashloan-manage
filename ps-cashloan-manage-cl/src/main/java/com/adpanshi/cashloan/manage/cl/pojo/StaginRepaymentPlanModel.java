package com.adpanshi.cashloan.manage.cl.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/***
 ** @category 分期还款计划...
 ** @author qing.yunhui
 ** @email: 280672161@qq.com
 ** @createTime: 2017年12月26日上午10:52:17
 **/
public class StaginRepaymentPlanModel implements Serializable{

	private static final long serialVersionUID = 1L;

	private String repayTime;//每期的还款日
	private String orderNo;//每期的子订单-订单号
	private BigDecimal interests;//每期的利息
	private BigDecimal amount;//每期的本金
	private String state;//每期的订单号
	private String borrowId;//子订单-主键id
	private BigDecimal totalAmount;//各分期总本金
	private BigDecimal penaltyAmout;//每期的逾期金额
	private BigDecimal fee;//每期的手续费
	private String bank;//银行名称
	private String cardNo;//银行卡号 
	private String byStages;//第N期
	private long repaymentDay;//距离还款日N天
	
	public String getRepayTime() {
		return repayTime;
	}
	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}
	public BigDecimal getInterests() {
		return interests;
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public void setInterests(BigDecimal interests) {
		this.interests = interests;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getBorrowId() {
		return borrowId;
	}
	public void setBorrowId(String borrowId) {
		this.borrowId = borrowId;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getPenaltyAmout() {
		return penaltyAmout;
	}
	public void setPenaltyAmout(BigDecimal penaltyAmout) {
		this.penaltyAmout = penaltyAmout;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getByStages() {
		return byStages;
	}
	public void setByStages(String byStages) {
		this.byStages = byStages;
	}
	public long getRepaymentDay() {
		return repaymentDay;
	}
	public void setRepaymentDay(long repaymentDay) {
		this.repaymentDay = repaymentDay;
	}
}
