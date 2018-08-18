package com.adpanshi.cashloan.manage.cl.service;


import java.util.List;
import java.util.Map;

import com.adpanshi.cashloan.manage.cl.pojo.DailyData;
import com.adpanshi.cashloan.manage.cl.pojo.DayPassApr;
import com.github.pagehelper.Page;

/**
 * 平台数据日报
 * @version 1.0
 * @date 2017年3月20日下午4:56:21
 */
public interface RiskDataService {

	/**
	 * 平台数据日报
	 * @param params
	 * @param current
	 * @param pageSize
	 * @return Page<DailyData>
	 */
	Page<DailyData> findDayDataNew(Map<String, Object> params, Integer current, Integer pageSize);


	/**
	 * 更新昨天日报数据
	 */
	void saveDayData();

	/**
	 * 平台数据日报导出
	 * @param params
	 * @return List
	 */
	List<?> listdailyData(Map<String, Object> params);

	/**
	 * 新每日通过率
	 * @param params
	 * @param current
	 * @param pageSize
	 * @return Page<DayPassApr>
	 */
    Page<DayPassApr> findDayAprNew(Map<String, Object> params, Integer current, Integer pageSize);
}
