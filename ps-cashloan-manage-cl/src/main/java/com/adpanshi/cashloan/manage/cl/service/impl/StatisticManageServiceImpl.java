package com.adpanshi.cashloan.manage.cl.service.impl;


import com.adpanshi.cashloan.manage.cl.mapper.expand.StatisticManageMapper;
import com.adpanshi.cashloan.manage.cl.pojo.DayNeedAmountModel;
import com.adpanshi.cashloan.manage.cl.pojo.IncomeAndExpendModel;
import com.adpanshi.cashloan.manage.cl.service.StatisticManageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 统计管理
 *
 * @version 1.1.0
 * @date 2017年3月21日下午4:42:31
 * Copyright 粉团网路 现金贷  All Rights Reserved
 * @update_date 2017/12/22
 * @updator huangqin
 */
@Service("statisticManageService")
public class StatisticManageServiceImpl implements StatisticManageService {
    private static final Logger logger = LoggerFactory.getLogger(StatisticManageServiceImpl.class);
    @Resource
    private StatisticManageMapper statisticManageMapper;

    @Override
    public Page<DayNeedAmountModel> dayNeedAmount(Map<String, Object> searcPahrams, Integer current, Integer pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<DayNeedAmountModel>) statisticManageMapper.dayNeedAmount(searcPahrams);
    }

    @Override
    public Page<IncomeAndExpendModel> repayIncomeAndExpend(Map<String, Object> searcPahrams, Integer current, Integer pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<IncomeAndExpendModel>) statisticManageMapper.repayIncomeAndExpend(searcPahrams);
    }
}
