package com.adpanshi.cashloan.manage.cl.service;

/**
 * @author 8470
 * @version 1.0.1
 * @date 2018/08/22 15:44:11
 * @desc 用户提额Service
 * Copyright 浙江盘石 All Rights Reserved
 */
public interface CreditsUpgradeService {

	/**
	 * <p>判断当前用户是否可以提额</p>
	 * <p>更新用户提额表</p>
	 * @param userId  待处理的用户
	 * @return boolean
	 * */
	boolean isCanCreditUpgrade(Long userId);

	/**
	 * <p>根据给定userId查找用户临时额度并计算用户剩余的临时额度之和</p>
	 * <p>可使用临时额度=临时额度总和  - 已使用临时额度总和</p>
	 * @param userId
	 * @return Double
	 * */
	Double getCreditsByUserId(Long userId);

}
