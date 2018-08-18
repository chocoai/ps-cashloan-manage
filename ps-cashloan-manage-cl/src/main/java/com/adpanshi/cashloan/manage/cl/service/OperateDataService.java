package com.adpanshi.cashloan.manage.cl.service;


import com.adpanshi.cashloan.manage.cl.pojo.OverdueAnalisisModel;
import com.adpanshi.cashloan.manage.cl.pojo.RepayAnalisisModel;
import com.github.pagehelper.Page;

import java.util.Map;

/**
 * 运营数据
 * @version 1.0
 * @date 2017年3月21日下午3:00:15
 */
public interface OperateDataService {

	/**
	 * 每月还款统计
	 * @param searchParams
	 * @param current
	 * @param pageSize
	 * @return ResponseEntity<ResultModel>
	 */
	Page<RepayAnalisisModel> monthRepayAnalisis(Map<String, Object> searchParams, Integer current, Integer pageSize);

	/**
	 * 每日还款统计
	 * @param searchParams
	 * @param current
	 * @param pageSize
	 * @return ResponseEntity<ResultModel>
	 */
	Page<RepayAnalisisModel> dayRepayAnalisis(Map<String, Object> searchParams, Integer current, Integer pageSize);

	/**
	 * 每月逾期统计
	 * @param searchParams
	 * @param current
	 * @param pageSize
	 * @return ResponseEntity<ResultModel>
	 */
	Page<OverdueAnalisisModel> overdueAnalisis(Map<String, Object> searchParams, Integer current, Integer pageSize);
}
