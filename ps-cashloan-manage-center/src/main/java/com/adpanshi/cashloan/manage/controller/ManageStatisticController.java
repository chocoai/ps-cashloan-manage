package com.adpanshi.cashloan.manage.controller;

import com.adpanshi.cashloan.manage.cl.pojo.DayNeedAmountModel;
import com.adpanshi.cashloan.manage.cl.pojo.IncomeAndExpendModel;
import com.adpanshi.cashloan.manage.cl.service.StatisticManageService;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.util.RdPage;
import com.adpanshi.cashloan.manage.pojo.ResultModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.github.pagehelper.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * 统计管理Controller
 *
 * @version 1.1.0
 * @date 2017年3月21日下午4:45:20
 * Copyright 粉团网路 现金贷  All Rights Reserved
 * @update_date 2017/12/22
 * @updator huangqin
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/statistic/")
public class ManageStatisticController extends ManageBaseController {

    @Resource
    private StatisticManageService statisticManageService;

    /**
     * 统计管理-每日未还本金-查询
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     * @throws Exception
     */
    @RequestMapping(value = "dayNeedAmount.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> dayNeedAmount(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                     @RequestParam(value = "current") Integer current,
                                                     @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        this.logger.info("------统计管理-每日未还本金-查询------");
        //读库标志
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<DayNeedAmountModel> page = statisticManageService.dayNeedAmount(params, current, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 统计管理-每日放款收支数据-查询
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     * @throws Exception
     */
    @RequestMapping(value = "incomeAndExpend.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> incomeAndExpend(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                       @RequestParam(value = "current") Integer current,
                                                       @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        this.logger.info("------统计管理-每日放款收支数据-查询------");
        //读库标志
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<IncomeAndExpendModel> page = statisticManageService.repayIncomeAndExpend(params, current, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
}
