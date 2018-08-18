package com.adpanshi.cashloan.manage.controller;


import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.manage.cl.enums.PayEnum;
import com.adpanshi.cashloan.manage.cl.model.expand.PayLogModel;
import com.adpanshi.cashloan.manage.cl.service.PayCheckService;
import com.adpanshi.cashloan.manage.cl.service.PayLogService;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.util.RdPage;
import com.adpanshi.cashloan.manage.pojo.ResultModel;
import com.github.pagehelper.Page;
import org.apache.commons.lang3.StringUtils;
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
 * 支付管理
 *
 * @version 2.0.0
 * @date 2017-12-22 18:22
 * @author: nmnl
 * Copyright 粉团网路 arc All Rights Reserved
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/pay/")
public class ManagePayController extends ManageBaseController {
    @Resource
    private PayLogService payLogService;

    /**
     * 支付信息下拉查询
     * @return payLogDropDownList.htm
     * */
    @RequestMapping(value = "payLogDropDownList.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> payLogDropDownList(){
        Map<String, Object> result = new HashMap<>();
        //支付类型
        result.put("payLogType", PayEnum.PAY_LOG_TYPE.EnumValueS());
        //支付状态
        result.put("payLogState",PayEnum.PAY_LOG_STATE.EnumValueS());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }


    /**
     * 支付信息:(支付记录,支付审核)
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @throws Exception
     * @author: nmnl
     * @date : 2017-12-22 20:06
     * /modules/manage/pay/log/page.htm
     */
    @RequestMapping(value = "payLogList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> payLogList(
            @RequestParam(value = "searchParams", required = false) String searchParams,
            @RequestParam(value = "current") Integer current,
            @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<PayLogModel> page = payLogService.page(current, pageSize, params);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
}
