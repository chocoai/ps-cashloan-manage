package com.adpanshi.cashloan.manage.controller;


import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.manage.cl.enums.BorrowEnum;
import com.adpanshi.cashloan.manage.cl.enums.ExamineEnum;
import com.adpanshi.cashloan.manage.cl.enums.UserEnum;
import com.adpanshi.cashloan.manage.cl.model.UserExamine;
import com.adpanshi.cashloan.manage.cl.service.BorrowAuditLogService;
import com.adpanshi.cashloan.manage.cl.service.BorrowService;
import com.adpanshi.cashloan.manage.cl.service.ChannelService;
import com.adpanshi.cashloan.manage.cl.service.UserExamineService;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
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
import java.util.HashMap;
import java.util.Map;

@Scope("prototype")
@Controller
@RequestMapping("/manage/examine/")
public class ManageExamineController extends ManageBaseController {

    @Resource
    UserExamineService userExamineService;
    @Resource
    private BorrowService BorrowService;
    @Resource
    private BorrowAuditLogService borrowAuditLogService;
    @Resource
    private ChannelService channelService;

    /****** 信审人员管理 ******//*

    *//**
     * @throws Exception
     * @description 我的信审订单-查询所有下拉条件
     * @author: huangqin
     * @date : 2018-01-01 22:42
     */
    @RequestMapping(value = "userExamineDropDownList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> userExamineDropDownList() throws Exception {
        Map<String, Object> result = new HashMap<>();
        //渠道查询
        result.put("stateList", ExamineEnum.EXAMINE_STATE.EnumValueS());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 信审专员信息查询
     *
     * @param searchParams
     * @param current
     * @param pageSize
     */
    @RequestMapping(value = "list.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> userExamineList(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                       @RequestParam(value = "current") Integer current,
                                                       @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        logger.info("------信审管理-信审人员管理-查询------");
        Map<String, Object> params = parseToMap(searchParams, true);
        Map<String, Object> result = new HashMap<String, Object>();
        Page<UserExamine> page = userExamineService.listUserExamineInfo(params, current, pageSize);
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 信审专员信息变更
     *
     * @param id
     * @param status
     */
    @RequestMapping(value = "update.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> userExamineUpdateState(@RequestParam(value = "id") Long id, @RequestParam(value = "status") String status) {
        AuthUserRole sysUser = this.getAuthUserRole();
        if (StringUtil.isEmpty(id) || id.compareTo(0L) <= 0) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultModel.ok(userExamineService.updateUserExamineInfo(sysUser, id,status)), HttpStatus.OK);
    }

    /**
     * 未添加信审人员查询
     *
     * @return esponseEntity<ResultModel>
     */
    @RequestMapping(value = "listAddUser.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> listAddUser() {
        logger.info("------信审管理-信审人员管理-未添加信审人员查询------");
        return new ResponseEntity<>(ResultModel.ok(userExamineService.listadduser()), HttpStatus.OK);
    }

    /**
     * 添加信审人员
     *
     * @param userIds
     * @return ResponseEntity<ResultModel>
     */
    @RequestMapping(value = "save.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> examineSave(@RequestParam(value = "userIds")Long ... userIds) throws Exception {
        if (null == userIds || userIds.length <= 0) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        userExamineService.addUserExamInfo(userIds);
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }


    /***** 我的信审订单 ******/

    /**
     * @throws Exception
     * @description 我的信审订单-查询所有下拉条件
     * @author: huangqin
     * @date : 2018-01-01 22:35
     */
    @RequestMapping(value = "reviewDropDownList.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> searchReviewBorrowList() throws Exception {
        Map<String, Object> result = new HashMap<>();
        //渠道查询
        result.put("channelList", channelService.listChannel());
        //注册客户端
        result.put("clientList", UserEnum.USER_CLIENT.EnumValueS());
        //订单状态
        result.put("borrowStateList", BorrowEnum.BORROW_STATE.EnumValueS());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
}
