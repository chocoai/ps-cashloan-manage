package com.adpanshi.cashloan.manage.cl.model.expand;


import com.adpanshi.cashloan.manage.cl.model.BorrowRepay;

import java.util.Date;

/**
 * 还款计划Model
 * @author Vic Tang
 * @date 2017-02-14 13:42:32
 *
 */
public class BorrowRepayModel extends BorrowRepay {

	private static final long serialVersionUID = 1L;
	
	/** 还款方式 - 正常还款 */
	public static final String NORMAL_REPAYMENT = "10";

	/** 还款方式 - 逾期减免 */
	public static final String OVERDUE_RELIEF = "20";

	/** 还款方式 - 逾期正常还款 */
	public static final String OVERDUE_REPAYMENT = "30";
	
	
	/** 还款状态 -已还款 */
	public static final String STATE_REPAY_YES = "10";
	
	/** 还款状态 - 未还款 */
	public static final String STATE_REPAY_NO = "20";
	
	/**
	 * 借款时间
	 */
	private String createTime;

	/**
	 * 还款时间格式化 (yyyy-MM-dd HH:mm)
	 */
	private String repayTimeStr;
	/**
	 * 实际还款时间
	 */
	private String realRepayTime;
	
	/**
	 * 实际还款金额
	 */
	private String realRepayAmount;
	
	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 手机号码
	 */
	private String phone;
	/**
	 * 订单号
	 */
	private String orderNo;


	/**
	 * 还款金额
	 */
	private Double repayAmount;


	/**
	 * 借款时间
	 */
	private Date borrowTime;
	/**
	 * 应还款日期 zy
	 *
	 */
	private  Date  repayPlanTime;



	/**
	 * 复审人姓名
	 * @return
	 */
	private  String auditName;/*zy*/

	/**
	 * 审核生成时间
	 * @return
	 */
	private  Date auditTime;

	/**借款期限*/
	private String timeLimit;

	/**
	 * 实际还款金额
	 */
	private Double realAmout;

	/**
	 * 实际还款罚金
	 */
	private Double realPenaltyAmout;

	/**订单状态*/
	private String borrowState;

	/**还款方式*/
	private String repayWay;

	/**还款帐号*/
	private String repayAccount;

	/**还款流水号*/
	private String serialNumber;

	/**期数*/
	private Integer phase;


	public String getBorrowAmount() {
		return borrowAmount;
	}

	public void setBorrowAmount(String borrowAmount) {
		this.borrowAmount = borrowAmount;
	}

	/**
	 * 借款金额
	 */

	private String borrowAmount;

	/**
	 * 应还总额
	 */

	private String repayTotal;

	/**
	 *确认还款时间
	 */
	private Date confirmTime;

	/**
	 *确认还款人
	 */
	private String confirmName;

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	/**
	 *确认还款人
	 */
	private String idNo;

	/**
	 * 获取还款时间格式化(yyyy-MM-ddHH:mm)
	 * 
	 * @return repayTimeStr
	 */
	public String getRepayTimeStr() {
		return repayTimeStr;
	}

	/**
	 * 设置还款时间格式化(yyyy-MM-ddHH:mm)
	 * 
	 * @param repayTimeStr
	 */
	public void setRepayTimeStr(String repayTimeStr) {
		this.repayTimeStr = repayTimeStr;
	}

	public String getRealRepayTime() {
		return realRepayTime;
	}

	public void setRealRepayTime(String realRepayTime) {
		this.realRepayTime = realRepayTime;
	}

	public String getRealRepayAmount() {
		return realRepayAmount;
	}

	public void setRealRepayAmount(String realRepayAmount) {
		this.realRepayAmount = realRepayAmount;
	}

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {this.realName = realName;}
	public String getOrderNo() {return orderNo;}

	public void setOrderNo(String orderNo) {this.orderNo = orderNo;}

	public Double getRepayAmount() {
		return repayAmount;
	}

	public void setRepayAmount(Double repayAmount) {
		this.repayAmount = repayAmount;
	}

	public Date getBorrowTime() {
		return borrowTime;
	}

	public void setBorrowTime(Date borrowTime) {
		this.borrowTime = borrowTime;
	}

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}

	public Double getRealAmout() {
		return realAmout;
	}

	public void setRealAmout(Double realAmout) {
		this.realAmout = realAmout;
	}

	public Double getRealPenaltyAmout() {
		return realPenaltyAmout;
	}

	public void setRealPenaltyAmout(Double realPenaltyAmout) {
		this.realPenaltyAmout = realPenaltyAmout;
	}

	public String getBorrowState() {
		return borrowState;
	}

	public void setBorrowState(String borrowState) {
		this.borrowState = borrowState;
	}

	public String getRepayWay() {
		return repayWay;
	}

	public void setRepayWay(String repayWay) {
		this.repayWay = repayWay;
	}

	public String getRepayAccount() {
		return repayAccount;
	}

	public void setRepayAccount(String repayAccount) {
		this.repayAccount = repayAccount;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getPhase() {
		return phase;
	}

	public void setPhase(Integer phase) {
		this.phase = phase;
	}

	public String getRepayTotal() {return repayTotal;}
	public void setRepayTotal(String repayTotal) {this.repayTotal = repayTotal;}

	public Date getRepayPlanTime() {return repayPlanTime;}
	public void setRepayPlanTime(Date repayPlanTime) {this.repayPlanTime = repayPlanTime;}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getConfirmName() {
		return confirmName;
	}

	public void setConfirmName(String confirmName) {
		this.confirmName = confirmName;
	}
}

