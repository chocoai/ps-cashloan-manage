package com.adpanshi.cashloan.manage.controller;



import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.manage.cl.enums.OpinionEnum;
import com.adpanshi.cashloan.manage.cl.enums.UserEnum;
import com.adpanshi.cashloan.manage.cl.model.Opinion;
import com.adpanshi.cashloan.manage.cl.model.UserBaseInfo;
import com.adpanshi.cashloan.manage.cl.model.expand.OpinionModel;
import com.adpanshi.cashloan.manage.cl.model.expand.UserBaseInfoModel;
import com.adpanshi.cashloan.manage.cl.pojo.AuthUserModel;
import com.adpanshi.cashloan.manage.cl.service.ChannelService;
import com.adpanshi.cashloan.manage.cl.service.OpinionService;
import com.adpanshi.cashloan.manage.cl.service.UserBaseInfoService;
import com.adpanshi.cashloan.manage.cl.service.UserService;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户(客户)管理 Controller
 *
 * @version 2.0.0
 * @date 2017-12-16 10:38:33
 * @author: nmnl
 * Copyright 粉团网路 arc All Rights Reserved
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/user/")
public class ManageUserController extends ManageBaseController {
    @Resource
    private UserService cloanUserService;
    @Resource
    private UserBaseInfoService userBaseInfoService;
    @Resource
    private ChannelService channelService;
    @Resource
    private OpinionService opinionService;

    /**
     * （用户列表信息）
     * 查询所有下拉条件
     *
     * @throws Exception
     * @author: nmnl
     * @date : 2017-12-16 11:55:03
     */
    @RequestMapping(value = "searchList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> searchList() {
        Map<String, Object> result = new HashMap<>();
        //用户渠道列表
        result.put("channelList", channelService.listChannel());
        //用户客户端列表
        result.put("clientList", UserEnum.USER_CLIENT.EnumValueS());
        //用户是否是黑名单状态
        result.put("userStateList", UserEnum.USER_STATE.EnumValueS());
        //意见反馈状态
        result.put("opinionStateList", OpinionEnum.OPINION_STATE.EnumValueS());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 用户管理->用户信息列表
     *
             * @param searchParams
     * @param currentPage
     * @param pageSize
     * @throws Exception
     * @author: nmnl
     * @date: 2017-12-16
            */
    @RequestMapping(value = "list.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> list(@RequestParam(value = "searchParams", required = false) String searchParams,
                                            @RequestParam(value = "current") Integer currentPage,
                                            @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        //读库标志
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<UserBaseInfoModel> page = cloanUserService.listUser(params, currentPage, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }


    /**
     * 用户管理->添加和取消黑名单
     *
     * @param userId
     * @param remarks
     * @author: nmnl
     * @date : 2017-12-16 11:55:03
     */
    @RequestMapping(value = "updState.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> updState(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "remarks") String remarks) {
        AuthUserRole authUserRole = getAuthUserRole();
        //校验入参
        if (null == userId || 0 == userId || StringUtils.isEmpty(remarks)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultModel.ok(userBaseInfoService.updateState(userId, remarks, authUserRole)), HttpStatus.OK);
    }

    /**
     * 用户管理-> 注销用户(暂时不提供...考虑中!)
     *
     * @param userId
     * @param remarks
     * @author: nmnl
     * @date : 2017-12-26 15:13:03
     */
    @RequestMapping(value = "updCancellation.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> updCancellation(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "remarks") String remarks) {
        //AuthUserRole authUserRole = getAuthUserRole();
        //校验入参
        if (null == userId || 0 == userId || StringUtils.isEmpty(remarks)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        //userBaseInfoService.updateState(userId,remarks,authUserRole);
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }


    /**
     * 复借用户信息列表
     *
     * @param searchParams
     * @param currentPage
     * @param pageSize
     * @throws Exception
     * @author: nmnl
     * @date: 2017-12-16 12:04:11
     */
    @RequestMapping(value = "reList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> relist(@RequestParam(value = "searchParams", required = false) String searchParams,
                                              @RequestParam(value = "current") Integer currentPage,
                                              @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        //查询条件
        Map<String, Object> params = parseToMap(searchParams, true);
        Map<String, Object> result = new HashMap<String, Object>();
        Page<UserBaseInfoModel> page = cloanUserService.listReUser(params, currentPage, pageSize);
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }


    /**
     * @param searchParams
     * @param current
     * @param pageSize
     * @throws Exception
     * @description 意见反馈分页查询
     * @author: nmnl
     * @date: 2017-12-16 12:04:11
     */
    @RequestMapping(value = "opinionList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> page(
            @RequestParam(value = "searchParams", required = false) String searchParams,
            @RequestParam(value = "current") Integer current,
            @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<OpinionModel> page = opinionService.page(params, current, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        result.put(Constant.RESPONSE_CODE, ManageExceptionEnum.SUCCEED_CODE_VALUE.Code());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @param id
     * @description 意见反馈记录查询
     * @author: nmnl
     * @date: 2017-12-16 12:04:11
     */
    @RequestMapping(value = "opinionView.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> view(@RequestParam(value = "id", required = false) Long id) throws Exception {
        return new ResponseEntity<>(ResultModel.ok(opinionService.getById(id)), HttpStatus.OK);
    }

    /**
     * @param id
     * @param feedback
     * @description 意见反馈处理
     */
    @RequestMapping(value = "opinionConfirm.htm", method = RequestMethod.POST)
    public ResponseEntity<ResultModel> opinionConfirm(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "feedback") String feedback
    ) throws Exception {
        AuthUserRole user = getAuthUserRole();
        if (null == user) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        Opinion opinion = new Opinion();
        opinion.setConfirmTime(new Date());
        opinion.setId(id);
        opinion.setSysUserId(user.getUserId());
        opinion.setFeedback(feedback);
        opinion.setState(OpinionModel.STATE_CONFIRMED);
        return new ResponseEntity<>(ResultModel.ok(opinionService.updateSelective(opinion)), HttpStatus.OK);
    }

    /**
     * 用户管理->用户认证信息
     *
     * @param searchParams
     * @param currentPage
     * @param pageSize
     * @throws Exception
     * @author: minge
     * @date: 2018-6-6
     */
    @RequestMapping(value = "authlist.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> authlist(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                @RequestParam(value = "current") Integer currentPage,
                                                @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        //读库标志
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<AuthUserModel> page = cloanUserService.listAuthUser(params, currentPage, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

}