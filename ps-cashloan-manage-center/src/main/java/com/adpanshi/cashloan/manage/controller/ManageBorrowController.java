package com.adpanshi.cashloan.manage.controller;

import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.manage.cl.enums.BorrowEnum;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowModel;
import com.adpanshi.cashloan.manage.cl.service.BorrowService;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.util.JsonUtil;
import com.adpanshi.cashloan.manage.core.common.util.RdPage;
import com.adpanshi.cashloan.manage.core.common.util.ServletUtils;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 借款订单controller
 * @date 2018/8/1 14:24
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/borrow")
public class ManageBorrowController  extends ManageBaseController {
    @Resource
    private BorrowService borrowService;
    /**
     * 贷后信息查询所有下拉条件
     *
     * @return ResponseEntity<ResultModel>
     * @author: huangqin
     * @date : 2018-01-04
     */
    @RequestMapping(value = "dropDownList.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> repayListDropDownList() {
        logger.info("------贷后管理-贷后信息下拉查询------");
        Map<String, Object> result = new HashMap<>();
        //贷后状态
        Map<String,String> borrowStateList=new HashMap<>();
        borrowStateList.put(BorrowModel.STATE_FINISH,BorrowModel.STATE_FINISH);
        borrowStateList.put(BorrowModel.STATE_DELAY,BorrowModel.STATE_DELAY);
        borrowStateList.put(BorrowModel.STATE_BAD,BorrowModel.STATE_BAD);
        result.put("stateList", borrowStateList);
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
    /**
     * 借款还款信息列表     
     * @param searchParams
     * @param current
     * @param pageSize
     */
    @RequestMapping(value="borrowRepayList.htm",method={RequestMethod.GET,RequestMethod.POST})
    public ResponseEntity<ResultModel> borrowRepayList(@RequestParam(value="searchParams",required=false) String searchParams,
                                                       @RequestParam(value = "current") int current,
                                                       @RequestParam(value = "pageSize") int pageSize){
        Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
        List stateList;
        if (params != null) {
            //放款列表
            String type= StringUtil.isNull(params.get("type"));
            if("repay".equals(type)){
                stateList = Arrays.asList(
                        BorrowModel.STATE_AUTO_PASS,
                        BorrowModel.STATE_PASS,
                        BorrowModel.STATE_REPAY_FAIL,
                        BorrowModel.STATE_REPAY,
                        BorrowModel.STATE_REMISSION_FINISH,
                        BorrowModel.STATE_DELAY,
                        BorrowModel.STATE_BAD,
                        BorrowModel.STATE_FINISH);
                params.put("stateList", stateList);
                String state = StringUtil.isNull(params.get("state"));
                if (null != state &&!StringUtil.isBlank(state)) {
                    params.put("state", state);
                }
            }
            String state = StringUtil.isNull(params.get("state"));
            if (null != state &&!StringUtil.isBlank(state)) {
                //还款列表
                if(state.equals(BorrowModel.STATE_FINISH)){//40
                    stateList = Arrays.asList(BorrowModel.STATE_FINISH,
                            BorrowModel.STATE_REMISSION_FINISH);
                    params.put("stateList", stateList);
                    params.put("state", "");
                }
                //逾期中列表  
                if(state.equals(BorrowModel.STATE_DELAY)){//50
                    stateList = Arrays.asList(BorrowModel.STATE_DELAY);
                    params.put("stateList", stateList);
                    params.put("state", "");
                }
                //坏账列表  
                if(state.equals(BorrowModel.STATE_BAD)){//90
                    stateList = Arrays.asList(BorrowModel.STATE_BAD);
                    params.put("stateList", stateList);
                    params.put("state", "");
                }
            }
        }
        Page<BorrowModel> page = borrowService.listBorrowModel(params,current,pageSize);
        Map<String,Object> result = new HashMap<String,Object>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
    /**
     * 借款还款信息详细页面
     * @param borrowId
     */
    @RequestMapping(value="/borrowRepayContent.htm",method={RequestMethod.GET,RequestMethod.POST})
    public void borrowRepayContent(@RequestParam(value="borrowId") long borrowId){
        BorrowModel model = borrowService.getModelByBorrowId(borrowId);
        Map<String,Object> result = new HashMap<String,Object>();
        result.put(Constant.RESPONSE_DATA, model);
        result.put(Constant.RESPONSE_CODE, ManageExceptionEnum.SUCCEED_CODE_VALUE.Code());
        result.put(Constant.RESPONSE_CODE_MSG, "获取成功");
        ServletUtils.writeToResponse(response,result);
    }
}
