package com.adpanshi.cashloan.manage.controller;


import com.adpanshi.cashloan.manage.cl.pojo.OverdueAnalisisModel;
import com.adpanshi.cashloan.manage.cl.pojo.RepayAnalisisModel;
import com.adpanshi.cashloan.manage.cl.service.OperateDataService;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.util.RdPage;
import com.adpanshi.cashloan.manage.pojo.ResultModel;
import com.github.pagehelper.Page;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * 运营数据Controller
 *
 * @version 1.1.0
 * @date 2017年3月21日下午2:59:46
 * @update_date 2017/12/22
 * @updator huangqin
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/operateData/")
public class ManageOperateDataController extends ManageBaseController {

    @Resource
    private OperateDataService operateDataService;

    /**
     * 每日还款统计
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     * @throws Exception
     */
    @RequestMapping(value = "dayRepay.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> dayRepay(@RequestParam(value = "searchParams") String searchParams,
                                                @RequestParam(value = "current") Integer current,
                                                @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        this.logger.info("------运营数据-每日还款统计-查询------");
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<RepayAnalisisModel> page = operateDataService.dayRepayAnalisis(params, current, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 每月还款统计
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     * @throws Exception
     */
    @RequestMapping(value = "monthRepay.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> monthRepay(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                  @RequestParam(value = "current") Integer current,
                                                  @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        this.logger.info("------运营数据-每月还款统计-查询------");
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<RepayAnalisisModel> page = operateDataService.monthRepayAnalisis(params, current, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 每月逾期分析
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     * @throws Exception
     */
    @RequestMapping(value = "monthOverdue.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> overdue(@RequestParam(value = "searchParams", required = false) String searchParams,
                                               @RequestParam(value = "current") Integer current,
                                               @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        this.logger.info("------运营数据-每月逾期分析-查询------");
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<OverdueAnalisisModel> page = operateDataService.overdueAnalisis(params, current, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
}