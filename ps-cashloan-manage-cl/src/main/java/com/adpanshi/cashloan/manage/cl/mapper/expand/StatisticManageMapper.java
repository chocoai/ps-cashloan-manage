package com.adpanshi.cashloan.manage.cl.mapper.expand;



import com.adpanshi.cashloan.common.mapper.RDBatisDao;
import com.adpanshi.cashloan.manage.cl.pojo.DayNeedAmountModel;
import com.adpanshi.cashloan.manage.cl.pojo.IncomeAndExpendModel;

import java.util.List;
import java.util.Map;

/**
 * 统计管理
 *
 * @version 1.0
 * @date 2017年3月21日下午4:39:37
 * Copyright 粉团网路 现金贷  All Rights Reserved
 * @update_date 2017/12/22
 * @updator huangqin
 */
@RDBatisDao
public interface StatisticManageMapper {

    /**
     * 每日收支记录
     *
     * @param params
     * @return List<IncomeAndExpendModel>
     */
    List<IncomeAndExpendModel> repayIncomeAndExpend(Map<String, Object> params);

    /**
     * 每日未还本金
     *
     * @param params
     * @return List<DayNeedAmountModel>
     */
    List<DayNeedAmountModel> dayNeedAmount(Map<String, Object> params);
}
