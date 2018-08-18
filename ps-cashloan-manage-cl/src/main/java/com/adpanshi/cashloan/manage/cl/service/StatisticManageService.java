package com.adpanshi.cashloan.manage.cl.service;



import com.adpanshi.cashloan.manage.cl.pojo.DayNeedAmountModel;
import com.adpanshi.cashloan.manage.cl.pojo.IncomeAndExpendModel;
import com.github.pagehelper.Page;

import java.util.Map;

/**
 * 统计管理Service
 *
 * @version 1.0.0
 * @date 2017年3月21日下午4:42:06
 * Copyright 粉团网路 现金贷  All Rights Reserved
 */
public interface StatisticManageService {

    /**
     * 每日收支记录
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @return Page<IncomeAndExpendModel>
     */
    Page<IncomeAndExpendModel> repayIncomeAndExpend(Map<String, Object> searchParams, Integer current, Integer pageSize);

    /**
     * 每日未还本金
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @return Page<DayNeedAmountModel>
     */
    Page<DayNeedAmountModel> dayNeedAmount(Map<String, Object> searchParams, Integer current, Integer pageSize);
}
