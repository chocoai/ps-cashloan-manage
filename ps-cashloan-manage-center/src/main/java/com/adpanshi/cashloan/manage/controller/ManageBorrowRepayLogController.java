package com.adpanshi.cashloan.manage.controller;

import com.adpanshi.cashloan.manage.cl.model.expand.BorrowRepayLogModel;
import com.adpanshi.cashloan.manage.cl.service.BorrowRepayLogService;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.util.RdPage;
import com.adpanshi.cashloan.manage.cl.enums.BorrowRepayEnum;
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
 * @author Vic Tang
 * @Description: 还款记录controller
 * @date 2018/8/1 9:56
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/borrowRepayLog/")
public class ManageBorrowRepayLogController extends ManageBaseController {
    @Resource
    private BorrowRepayLogService borrowRepayLogService;
    /**
     * 还款记录列表
     *
     * @param searchParams
     * @param currentPage
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     * @author: huangqin
     * @date : 2018-01-04
     */
    @RequestMapping(value = "list.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> repayLogList(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                    @RequestParam(value = "current") Integer currentPage,
                                                    @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        logger.info("------还款管理-还款记录查询------");
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<BorrowRepayLogModel> page = borrowRepayLogService.listModel(params, currentPage, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 还款记录查询所有下拉条件
     *
     * @return ResponseEntity<ResultModel>
     * @author: huangqin
     * @date : 2018-01-04
     */
    @RequestMapping(value = "dropDownList.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> repayLogDropDownList() {
        logger.info("------还款管理-还款记录下拉查询------");
        Map<String, Object> result = new HashMap<>();
        //还款状态
        result.put("repayState", BorrowRepayEnum.REPAY_STATE.EnumValueS());
        //还款方式
        result.put("repayWay", BorrowRepayEnum.REPAY_WAY.EnumValueS());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
}
