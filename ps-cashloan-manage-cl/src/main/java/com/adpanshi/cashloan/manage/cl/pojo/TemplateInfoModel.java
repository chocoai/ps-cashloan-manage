package com.adpanshi.cashloan.manage.cl.pojo;

import com.adpanshi.cashloan.manage.cl.model.expand.BorrowTemplateModel;
import com.adpanshi.cashloan.manage.core.common.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.MathUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;


/***
 ** @category 借款模板...
 ** @author qing.yunhui
 ** @email: 280672161@qq.com
 ** @createTime: 2017年12月23日上午11:13:11
 **/
public class TemplateInfoModel {
	
	/**总期数*/
	private Long stages;
	
	/**每期还款本金*/
	private BigDecimal amount;
	
	/**每期利息*/
	private BigDecimal interest;
	
	/**还款方式*/
	private String payType;
	
	/**日利率*/
	private BigDecimal dayRate;
	
	/**
	 * 借款期限
	 * */
	private String timeLimit; 
	
	/**分期周期(天)*/
	private Long cycle;
	
	public TemplateInfoModel(){}
	
	public TemplateInfoModel(Long stages, BigDecimal amount, BigDecimal interest, String payType, String timeLimit){
		this.stages=stages;
		this.amount=amount;
		this.interest=interest;
		this.payType=payType;
		this.timeLimit=timeLimit;
	}
	
	public TemplateInfoModel(Long stages, BigDecimal amount, BigDecimal interest, String payType, String timeLimit, BigDecimal dayRate, Long cycle){
		this(stages, amount, interest, payType, timeLimit);
		this.dayRate=dayRate;
		this.cycle=cycle;
	}
	
	/**
	 * <p>根据给定模板进行解释</p>
	 * @param borrowAmount
	 * @param templateInfo
	 * @return 
	 * */
	public TemplateInfoModel parseTemplateInfo(BigDecimal borrowAmount, String templateInfo){
		if(StringUtil.isBlank(templateInfo)){
			return null;
		}
		BorrowTemplateModel template = JSONObject.parseObject(templateInfo, BorrowTemplateModel.class);
		Long cycle = Long.parseLong(template.getCycle());
		Long timeLimit = Long.parseLong(template.getTimeLimit());
		Long stages = timeLimit / cycle;
		BigDecimal amount = borrowAmount.divide(new BigDecimal(stages), 2,RoundingMode.DOWN);
		BigDecimal interest = template.getFee().divide(new BigDecimal(stages),2, RoundingMode.DOWN);
		return new TemplateInfoModel(stages, amount, interest,null==template.getPayType()?"":template.getPayType(),template.getTimeLimit(),template.getDayRate(),cycle);
	}

	public Long getStages() {
		return stages;
	}

	public void setStages(Long stages) {
		this.stages = stages;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}

	public BigDecimal getDayRate() {
		return dayRate;
	}

	public void setDayRate(BigDecimal dayRate) {
		this.dayRate = dayRate;
	}

	public Long getCycle() {
		return cycle;
	}

	public void setCycle(Long cycle) {
		this.cycle = cycle;
	} 
	
}
