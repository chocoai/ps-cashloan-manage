package com.adpanshi.cashloan.manage.controller;

import com.adpanshi.cashloan.manage.cl.pojo.DailyData;
import com.adpanshi.cashloan.manage.cl.pojo.DayPassApr;
import com.adpanshi.cashloan.manage.cl.service.RiskDataService;
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
 * 风控数据Controller
 *
 * @version 1.1.0
 * @date 2017年3月20日下午4:54:48
 * @update_date 2017/12/22
 * @updator huangqin
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/riskData/")
public class ManageRiskDataController extends ManageBaseController {

    @Resource
    private RiskDataService riskDataService;

    /**
     * 每日通过率
     *
     * @param current
     * @param pageSize
     * @param searchParams
     * @throws Exception
     */
    @RequestMapping(value = "dayApr.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> dayApr(@RequestParam(value = "searchParams") String searchParams,
                                              @RequestParam(value = "current") Integer current,
                                              @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        this.logger.info("------风控数据-每日通过率-查询------");
        //读库标志
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<DayPassApr> page = riskDataService.findDayAprNew(params, current, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 平台数据日报
     *
     * @param current
     * @param pageSize
     * @param searchParams
     * @throws Exception
     */
    @RequestMapping(value = "dayData.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> dayDataNew(@RequestParam(value = "searchParams") String searchParams,
                                                  @RequestParam(value = "current") Integer current,
                                                  @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        this.logger.info("------风控数据-平台数据日报-查询------");
        //读库标志
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<DailyData> page = riskDataService.findDayDataNew(params, current, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
}
