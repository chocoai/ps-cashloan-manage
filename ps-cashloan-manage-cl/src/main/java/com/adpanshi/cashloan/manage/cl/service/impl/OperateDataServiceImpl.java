package com.adpanshi.cashloan.manage.cl.service.impl;


import com.adpanshi.cashloan.manage.cl.mapper.expand.OperateDataMapper;
import com.adpanshi.cashloan.manage.cl.pojo.OverdueAnalisisModel;
import com.adpanshi.cashloan.manage.cl.pojo.RepayAnalisisModel;
import com.adpanshi.cashloan.manage.cl.service.OperateDataService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 运营分析
 * @version 1.1.0
 * @date 2017年3月21日下午3:00:36
 * @update_date 2017/12/22
 * @updator huangqin
 */
@Service("systemAnalysisService")
public class OperateDataServiceImpl implements OperateDataService {

	@Resource
	private OperateDataMapper operateDataMapper;
	
	@Override
	public Page<RepayAnalisisModel> monthRepayAnalisis(Map<String, Object> searchParams, Integer current, Integer pageSize) {
		PageHelper.startPage(current, pageSize);
		if (null != searchParams )searchParams.put("dateType", "%Y-%m");
		return (Page<RepayAnalisisModel>) operateDataMapper.repayAnalisis(searchParams);
	}

	@Override
	public Page<RepayAnalisisModel> dayRepayAnalisis(Map<String, Object> searchParams, Integer current, Integer pageSize) {
		PageHelper.startPage(current, pageSize);
		if (null != searchParams )searchParams.put("dateType", "%Y-%m-%d");
		return (Page<RepayAnalisisModel>) operateDataMapper.repayAnalisis(searchParams);
	}

	@Override
	public Page<OverdueAnalisisModel> overdueAnalisis(Map<String, Object> searchParams, Integer current, Integer pageSize) {
		PageHelper.startPage(current, pageSize);
		if (null != searchParams )searchParams.put("dateType", "%Y-%m");
		return (Page<OverdueAnalisisModel>) operateDataMapper.overdueAnalisis(searchParams);
	}

}
