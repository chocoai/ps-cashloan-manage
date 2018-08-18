package com.adpanshi.cashloan.manage.controller;

import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.common.exception.ManageException;
import com.adpanshi.cashloan.manage.arc.service.SysUserService;
import com.adpanshi.cashloan.manage.cl.enums.BorrowEnum;
import com.adpanshi.cashloan.manage.cl.enums.UrgeRepayEnum;
import com.adpanshi.cashloan.manage.cl.model.UrgeRepayLog;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowModel;
import com.adpanshi.cashloan.manage.cl.model.expand.UrgeRepayCountModel;
import com.adpanshi.cashloan.manage.cl.model.expand.UrgeRepayOrderModel;
import com.adpanshi.cashloan.manage.cl.service.BorrowRepayService;
import com.adpanshi.cashloan.manage.cl.service.UrgeRepayLogService;
import com.adpanshi.cashloan.manage.cl.service.UrgeRepayOrderService;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
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
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 催收订单管理
 * @date 2018/8/1 20:15
 */
@Scope("prototype")
@Controller
@RequestMapping("/manage/")
public class ManageCollectionManageController extends ManageBaseController {
    @Resource
    private UrgeRepayOrderService urgeRepayOrderService;
    @Resource
    private UrgeRepayLogService urgeRepayLogService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private BorrowRepayService borrowRepayService;

    /**
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 催收下拉列表查询
     * @data 2017-12-26
     */
    @RequestMapping(value = "collManage/collDropDownList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> userDropDownList() {
        this.logger.info("------催收下拉列表查询------");
        //获取催收人员列表
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleName", "CollectionSpecialist");
        Map<String, Object> result = new HashMap<>();
        result.put("collUserList", sysUserService.getUserInfo(params));
        //催收状态
        result.put("collState", UrgeRepayEnum.COLL_STATE.EnumValueS());
        //逾期等级
        result.put("overdueLevel", UrgeRepayEnum.OVERDUE_LEVEL.EnumValueS());
        //分配状态
        result.put("destributeState", UrgeRepayEnum.DESTRIBUTE_STATE.EnumValueS());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
    /**
     * 催收人员列表
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     */
    @RequestMapping(value = "collManage/list.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> list(@RequestParam(value = "searchParams", required = false) String searchParams,
                                            @RequestParam(value = "current") Integer current,
                                            @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<UrgeRepayCountModel> page = urgeRepayOrderService.memberCount(params, current, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
    /**
     * 催收订单管理，催收订单-查询列表
     * @param searchParams
     * @param pageSize
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     */
    @RequestMapping(value = "collManage/collList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> collList(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                @RequestParam(value = "current") Integer current,
                                                @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<UrgeRepayOrderModel> page = urgeRepayOrderService.list(params, current, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
    /****** 催收反馈 ******/
    /**
     * 我的催收反馈记录信息列表
     * @param searchParams
     * @param current
     * @param pageSize
     */
    @RequestMapping(value="collManage/logList.htm",method={RequestMethod.POST})
    public ResponseEntity<ResultModel> loglist(@RequestParam(value="searchParams",required=false) String searchParams,
                                               @RequestParam(value = "current") Integer current,
                                               @RequestParam(value = "pageSize") Integer pageSize)throws Exception{
        Map<String, Object> params = parseToMap(searchParams,true);
        AuthUserRole sysUser = getAuthUserRole();
        Page<UrgeRepayOrderModel> page =urgeRepayOrderService.listModel(params,current,pageSize);
        Map<String,Object> result = new HashMap<String,Object>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
    /**
     * /**
     * 催回率统计
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     */
    @RequestMapping(value = "collManage/urgeCount.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> urgeCount(
            @RequestParam(value = "searchParams", required = false) String searchParams,
            @RequestParam(value = "current") Integer current,
            @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<UrgeRepayCountModel> page = urgeRepayOrderService.urgeCount(params, current, pageSize);
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 催收订单统计
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     */
    @RequestMapping(value = "collManage/orderCountt.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> orderCount(
            @RequestParam(value = "searchParams", required = false) String searchParams,
            @RequestParam(value = "current") Integer current,
            @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<UrgeRepayCountModel> page = urgeRepayOrderService.orderCount(params, current, pageSize);
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 催收员每日统计
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     */
    @RequestMapping(value = "collManage/memberDayCount.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> memberDayCount(
            @RequestParam(value = "searchParams", required = false) String searchParams,
            @RequestParam(value = "current") Integer current,
            @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<UrgeRepayCountModel> page = urgeRepayOrderService.memberDayCount(params, current, pageSize);
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /****** 催收订单 ******/
    /**
     * 催收订单列表
     * @param searchParams
     * @param current
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     */
    @RequestMapping(value="collSelfOrder/collectionList.htm",method={RequestMethod.POST})
    public ResponseEntity<ResultModel> collectionList(@RequestParam(value="searchParams",required=false) String searchParams,
                                                      @RequestParam(value = "current") Integer current,
                                                      @RequestParam(value = "pageSize") Integer pageSize)throws Exception{
        Map<String, Object> params = parseToMap(searchParams,true);
        AuthUserRole sysUser = getAuthUserRole();
        params.put("userId",sysUser.getUserId());
        Page<UrgeRepayOrderModel> page =urgeRepayOrderService.listManage(params,current,pageSize);
        Map<String,Object> result = new HashMap<String,Object>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }


    /**
     * 开始催收/重新催收 state传20
     * @param id
     * @param state
     * @return ResponseEntity<ResultModel>
     */
    @RequestMapping(value="collSelfOrder/updateOrderState.htm",method={RequestMethod.POST})
    public ResponseEntity<ResultModel> addOrder(@RequestParam(value = "id") Long id,@RequestParam(value = "state") String state) {
        urgeRepayOrderService.updateLate(id,state);
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /****** 催收反馈 ******/
    /**
     * 我的催收反馈记录信息列表
     * @param searchParams
     * @param current
     * @param pageSize
     */
    @RequestMapping(value="collSelfOrder/logList.htm",method={RequestMethod.POST})
    public ResponseEntity<ResultModel> MyLoglist(@RequestParam(value="searchParams",required=false) String searchParams,
                                                 @RequestParam(value = "current") Integer current,
                                                 @RequestParam(value = "pageSize") Integer pageSize)throws Exception{
        Map<String, Object> params = parseToMap(searchParams,true);
        AuthUserRole sysUser = getAuthUserRole();
        params.put("userId",sysUser.getUserId());
        Page<UrgeRepayOrderModel> page =urgeRepayOrderService.listModel(params,current,pageSize);
        Map<String,Object> result = new HashMap<String,Object>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /****** 已逾期未入催 ******/
    /**
     * 逾期借款未入催查询列表
     *
     * @param currentPage
     * @param pageSize
     * @param searchParams
     * @return ResponseEntity<ResultModel>
     */
    @RequestMapping(value = "collWarning/unCollBorrowlist.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> unCollBorrowlist(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                        @RequestParam(value = "current") Integer currentPage,
                                                        @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        Map<String, Object> params = parseToMap(searchParams, true);
        params.put("state", BorrowModel.STATE_DELAY);
        Page<BorrowModel> page = borrowRepayService.listModelNotUrge(params, currentPage, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 加入催收订单
     *
     * @param id
     * @return ResponseEntity<ResultModel>
     */
    @RequestMapping(value = "collWarning/addCollOrder.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> addCollOrder(@RequestParam(value = "id") Long id) throws Exception {
        urgeRepayOrderService.saveOrder(id);
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }


    /****** 未还款已出催,未分配催收员 ******/
    /**
     * 未还款已出催，未分配催收员-查询列表
     *
     * @param searchParams
     * @param pageSize
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     */
    @RequestMapping(value = "collWarning/unRepayBorrowlist.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> unRepayBorrowlist(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                         @RequestParam(value = "current") Integer current,
                                                         @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        return new ResponseEntity<>(ResultModel.ok(urgeRepayOrderList(searchParams, current, pageSize)), HttpStatus.OK);
    }

    /**
     * 未还款已出催-查看催收反馈记录
     *
     * @param id
     * @return esponseEntity<ResultModel>
     */
    @RequestMapping(value = "collWarning/listDetail.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> listDetail(@RequestParam(value = "id") Long id,
                                                  @RequestParam(value = "current") Integer current,
                                                  @RequestParam(value = "pageSize") Integer pageSize) {
        Page<UrgeRepayLog> page = urgeRepayLogService.getLogByParam(id,current,pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }


    /**
     * 未分配催收员-分配催收员-催款专员信息列表
     *
     * @return ResponseEntity<ResultModel>
     */
    @RequestMapping(value = "collWarning/collUserlist.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> sysUserlist() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("roleName", "CollectionSpecialist");
        List<Map<String, Object>> users = sysUserService.getUserInfo(params);
        Map<String, Object> result = new HashMap<>();
        result.put("data", users);
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 未分配催收员-分配催收员-分配
     *
     * @param id
     * @param userId
     * @param userName
     * @return ResponseEntity<ResultModel>
     */
    @RequestMapping(value = "collWarning/allotUser.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> allotUser(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "userName") String userName) throws Exception {
        Map<String, Object> params = new HashMap<>();
        //是待催收、催收中、承诺还款、坏账则不更改状态
        if (!urgeRepayOrderService.isCollector(id)) {
            params.put("state", UrgeRepayOrderModel.STATE_ORDER_WAIT);
        }
        params.put("id", id);
        params.put("userId", userId);
        params.put("userName", userName);
        if (urgeRepayOrderService.orderAllotUser(params) <= 0) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_INSERT_CODE_VALUE), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * 未分配催收员-分配催收员-批量分配
     *
     * @param id
     * @param userId
     * @param userName
     * @return ResponseEntity<ResultModel>
     */
    @RequestMapping(value = "collWarning/batchAllotUser.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> allotUser(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "id") Long... id) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("userName", userName);
        params.put("state", UrgeRepayOrderModel.STATE_ORDER_WAIT);
        urgeRepayOrderService.orderAllotUsers(params, id);
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    private Map<String, Object> urgeRepayOrderList(String searchParams, Integer current,Integer pageSize) throws ManageException {
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<UrgeRepayOrderModel> page = urgeRepayOrderService.list(params, current, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return result;
    }
}
