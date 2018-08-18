package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.OperatorReqLog;
import java.util.Map;

/**
 * 运营商认证中间表Service
 * 
 * @author
 * @version 1.0.0
 * @date 2017-03-01 16:27:59
 */
public interface OperatorReqLogService {

	OperatorReqLog findSelective(Map<String, Object> paramMap);
	
	/**
	 * 查找用户是否有认证记录
	 * @param userId
	 * @return
	 */
	String findOrderByUserId(Long userId);

	/**
	 * 查询用户是否可以进行运营商认证
	 * @param userId
	 * @return
	 */
	boolean checkUserOperator(long userId);

	/**
	 * 查询最后一条符合条件的记录
	 * @param paramMap
	 * @return
	 */
	OperatorReqLog findLastRecord(Map<String, Object> paramMap);

	/**
	 * 更新记录
	 */
	int updateSelectRecord(Map<String, Object> paramMap);
}
