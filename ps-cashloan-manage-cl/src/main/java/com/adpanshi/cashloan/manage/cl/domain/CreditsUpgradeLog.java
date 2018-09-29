package com.adpanshi.cashloan.manage.cl.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @category 用户额度提升表日志表
 *
 * @author 8470
 * @Since 2011-2017
 * @create 2017-10-30 20:46:14
 * @history
 */
@SuppressWarnings("serial")
public class CreditsUpgradeLog implements Serializable{
	
	/**
	 * @Fields id:主键id
	 */
	private Long id;

	/**
	 * @Fields 外键表ID
	 */
	private Long parentId;


	/**
	 * @Fields userId:用户id
	 */
	private Long userId;
	
	/**
	 * @Fields creditsLeve:额度等级(1.1级、2.2级、3.3级)
	 */
	private Integer creditsLeve;
	
	/**
	 * @Fields credits:额度
	 */
	private Double credits;
	
		/**
	 * @Fields createTime:创建时间
	 */
	private Date createTime;
	
	/**
	 * @Fields updateTime:修改时间
	 */
	private Date updateTime;


	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getCreditsLeve() {
		return creditsLeve;
	}

	public void setCreditsLeve(Integer creditsLeve) {
		this.creditsLeve = creditsLeve;
	}

	public Double getCredits() {
		return credits;
	}

	public void setCredits(Double credits) {
		this.credits = credits;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}