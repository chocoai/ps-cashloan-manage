package com.adpanshi.cashloan.manage.controller;


import com.adpanshi.cashloan.manage.cl.model.expand.BorrowProgressModel;
import com.adpanshi.cashloan.manage.cl.service.BorrowProgressService;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.util.JsonUtil;
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
 * @author Vic Tang
 * @Description: 订单进度controller
 * @date 2018/8/1 14:46
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/borrowProgress/")
public class ManageBorrowProgressController extends ManageBaseController{
    @Resource
    private BorrowProgressService borrowProgressService;
    /**
     *借款进度列表
     * @param searchParams
     * @param currentPage
     * @param pageSize
     */
    @RequestMapping(value="list.htm",method={RequestMethod.GET,RequestMethod.POST})
    public ResponseEntity<ResultModel> progressList(@RequestParam(value="searchParams",required=false) String searchParams,
                                                    @RequestParam(value = "current") int currentPage,
                                                    @RequestParam(value = "pageSize") int pageSize){
        logger.info("------订单管理-订单进度查询------");
        Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
        Page<BorrowProgressModel> page =borrowProgressService.listModel(params,currentPage,pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
}
