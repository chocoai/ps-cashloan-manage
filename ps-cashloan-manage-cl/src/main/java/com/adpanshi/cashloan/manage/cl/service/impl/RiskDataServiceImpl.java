package com.adpanshi.cashloan.manage.cl.service.impl;



import com.adpanshi.cashloan.manage.cl.mapper.expand.DailyDataMapper;
import com.adpanshi.cashloan.manage.cl.pojo.DailyData;
import com.adpanshi.cashloan.manage.cl.pojo.DayPassApr;
import com.adpanshi.cashloan.manage.cl.service.RiskDataService;
import com.adpanshi.cashloan.manage.core.common.context.ExportConstant;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * 平台数据日报
 * @version 1.1.0
 * @date 2017年3月20日下午4:56:46
 * Copyright 粉团网路 现金贷  All Rights Reserved
 * @update_date 2017/12/22
 * @updator huangqin
 */
@Service("systemRcService")
public class RiskDataServiceImpl implements RiskDataService {
	private static final Logger logger = LoggerFactory.getLogger(RiskDataServiceImpl.class);
	@Resource
	private DailyDataMapper dailyDataMapper;

	@Override
	public Page<DailyData> findDayDataNew(Map<String, Object> params, Integer current, Integer pageSize) {
		PageHelper.startPage(current, pageSize);
		return (Page<DailyData>) dailyDataMapper.dayData(params);
	}

	@Override
	public void saveDayData() {
		dailyDataMapper.saveDayData();
	}

	@Override
	public List<?> listdailyData(Map<String, Object> params) {
		params.put("totalCount", ExportConstant.TOTAL_LIMIT);
		return dailyDataMapper.dayData(params);
	}

	@Override
	public Page<DayPassApr> findDayAprNew(Map<String, Object> params, Integer current, Integer pageSize) {
		PageHelper.startPage(current, pageSize);
		return (Page<DayPassApr>) dailyDataMapper.dayApr(params);
	}
}
