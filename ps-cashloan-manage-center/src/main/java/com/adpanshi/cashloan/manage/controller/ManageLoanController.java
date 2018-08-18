package com.adpanshi.cashloan.manage.controller;


import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.manage.cl.enums.BorrowEnum;
import com.adpanshi.cashloan.manage.cl.model.BorrowMainProgress;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowMainModel;
import com.adpanshi.cashloan.manage.cl.service.BorrowMainService;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.util.MapUtil;
import com.adpanshi.cashloan.manage.core.common.util.RdPage;
import com.adpanshi.cashloan.manage.core.common.util.StringUtil;
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
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 借款信息Controller
 * @version 2.0.0
 * @date: 2017-12-21 20:40:33
 * @author: nmnl
 * Copyright 粉团网路 arc All Rights Reserved
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/loan/")
public class ManageLoanController extends ManageBaseController {
    @Resource
    private BorrowMainService borrowMainService;

    /**
     * （机审列表）
     * 查询所有下拉条件
     *
     * @author: nmnl
     * @date : 2017-12-19 19:57:03
     */
    @RequestMapping(value = "searchBorrowRepayList.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> searchBorrowList() {
        Map<String, Object> result = new HashMap<>(16);
        //全部订单状态
        result.put("allBorrowState", BorrowEnum.ALL_BORROW_STATE.EnumValueS());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * （机审列表）
     * 查询所有下拉条件
     *
     * @author: nmnl
     * @date : 2017-12-19 19:57:03
     */
    @RequestMapping(value = "searchRefundBorrowList.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> searchRefundBorrowList() {
        Map<String, Object> result = new HashMap<>(16);
        //全部订单状态
        result.put("allBorrowState", BorrowEnum.REFUND_BORROW_STATE.EnumValueS());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 借款/放款:列表
     *
     * @param searchParams
     * @param current
     * @param pageSize     /modules/manage/borrow/borrowRepayList.htm
     * @throws Exception
     * @author: nmnl
     * @date : 2017-12-21 16:34:03
     */
    @RequestMapping(value = "borrowRepayList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> borrowRepayList(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                       @RequestParam(value = "current") Integer current,
                                                       @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        /** 借款订单状态key */
        String stateKey = "state";
        Map<String, Object> params = parseToMap(searchParams, true);
        //状态不为空,查询当前状态.
        if (null != params.get(stateKey)) {
            params.put("stateList", Arrays.asList(params.get(stateKey)));
        }
        //清除state状态
        params.remove(stateKey);
        Page<BorrowMainModel> borrowMainPage = (Page<BorrowMainModel>)borrowMainService.selectBorrowList(params, current, pageSize);
        Map<String, Object> result = new HashMap<>(16);
        result.put(Constant.RESPONSE_DATA, borrowMainPage);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(borrowMainPage));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }



    /**
     * 借款/放款:详细页面(需要重构,需要初步调试通过..)
     *
     * @param borrowMainId /modules/manage/borrow/borrowRepayContent.htm
     * @throws Exception
     * @author: nmnl
     * @date : 2017-12-21 21:06
     */
    @RequestMapping(value = "borrowRepayContent.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> borrowRepayContent(@RequestParam(value = "borrowMainId") Long borrowMainId) {
        if (null == borrowMainId) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        Map <String,Object> params=new HashMap(16);
        params.put("id",borrowMainId);
        List<BorrowMainModel> bmmList = borrowMainService.selectBorrowList(params,1,1);
        if(!StringUtil.isEmpty(bmmList)){
            BorrowMainModel bmm = bmmList.get(0);
            try {
                if(BorrowEnum.ALL_BORROW_STATE.STATE_FINISH.EnumKey().equals(bmm.getState())){
                    Map resultMap = MapUtil.convertBean(bmm);
                    Map brlm = borrowMainService.qryRepayLog(bmm.getId());
                    if(brlm!=null) {
                        resultMap.put("repayAmount", brlm.get("amount"));
                        resultMap.put("repayTime", brlm.get("repay_time"));
                        resultMap.put("penaltyAmout", brlm.get("penalty_amout"));
                        resultMap.put("penaltyDay", brlm.get("penalty_day"));
                    }
                    List list = new ArrayList();
                    list.add(resultMap);
                    return new ResponseEntity<>(ResultModel.ok(list), HttpStatus.OK);
                }
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(ResultModel.ok(bmmList), HttpStatus.OK);

    }


}
