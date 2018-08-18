package com.adpanshi.cashloan.manage.controller;


import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.manage.arc.model.SysUser;
import com.adpanshi.cashloan.manage.arc.service.SysUserService;
import com.adpanshi.cashloan.manage.cl.enums.BorrowEnum;
import com.adpanshi.cashloan.manage.cl.enums.ExamineEnum;
import com.adpanshi.cashloan.manage.cl.enums.RiskEnum;
import com.adpanshi.cashloan.manage.cl.enums.UserEnum;
import com.adpanshi.cashloan.manage.cl.model.*;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowMainModel;
import com.adpanshi.cashloan.manage.cl.service.*;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import com.adpanshi.cashloan.manage.core.common.util.RdPage;
import com.adpanshi.cashloan.manage.pojo.ResultModel;
import com.github.pagehelper.Page;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;

/**
 * 风控管理 Controller
 * @version 2.0.0
 * @date 2017-12-19 10:38:33
 * @author: nmnl
 * Copyright 粉团网路 arc All Rights Reserved
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/risk/")
public class ManageRiskController extends ManageBaseController {

    @Resource
    private UserExamineService userExamineService;
    @Resource
    private ChannelService channelService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private UserContactsMatchDictService userContactsMatchDictService;
    @Resource
    private OverduePhoneService overduePhoneService;
    @Resource
    private UserIdnoMatchDictService userIdnoMatchDictService;
    @Resource
    private BorrowMainService borrowMainService;
    @Resource
    private BorrowAuditLogService borrowAuditLogService;
    /**
     * （机审列表）
     * @description 查询所有下拉条件
     *
     * @author: nmnl
     * @date : 2017-12-19 19:57:03
     */
    @RequestMapping(value = "searchBorrowList.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> searchBorrowList() {
        Map<String, Object> result = new HashMap<>();
        //渠道查询
        result.put("channelList", channelService.listChannel());
        //注册客户端
        result.put("clientList", UserEnum.USER_CLIENT.EnumValueS());
        //审核状态-类型
        result.put("borrowStateList", BorrowEnum.BORROW_AUTO_TYPE.EnumValueS());
        //是否是黑名单
        result.put("blackState", UserEnum.USER_STATE.EnumValueS());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }


    /**
     * @description 借款机审列表 (需要重构,需要初步调试通过..)
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @date : 2017-12-19 16:38:22
     * @author: nmnl
     * @throws Exception
     */
    @RequestMapping(value = "borrowList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> borrowList(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                  @RequestParam(value = "current") Integer current,
                                                  @RequestParam(value = "pageSize") Integer pageSize) throws Exception{
        Map<String, Object> params = parseToMap(searchParams,true);
        //状态不为空,查询当前状态.{
        if (null == params.get("state") || StringUtils.isEmpty(String.valueOf(params.get("state")))) {
            params.put("stateList", BorrowEnum.BORROW_AUTO_TYPE.EnumValueS().keySet());
        }
        Page<BorrowMainModel> borrowMainPage = (Page<BorrowMainModel>)borrowMainService.selectBorrowList(params, current, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, borrowMainPage);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(borrowMainPage));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * （人工复审）
     * @description 查询所有下拉条件
     *
     * @throws Exception
     * @author: nmnl
     * @date : 2017-12-19 19:57:03
     * 渠道   modules/manage/promotion/channel/listChannel.htm
     * 分配人 modules/manage/examine/list.htm
     * 审核人 modules/manage/system/user/findUserName.htm
     */
    @RequestMapping(value = "searchReviewBorrowList.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> searchReviewBorrowList() throws Exception {
        Map<String, Object> result = new HashMap<>();
        //渠道查询
        result.put("channelList", channelService.listChannel());
        //注册客户端
        result.put("clientList", UserEnum.USER_CLIENT.EnumValueS());
        //订单状态
        result.put("borrowStateList", BorrowEnum.BORROW_STATE.EnumValueS());
        //审核人
        Map<String, Object> param = new HashMap<>();
        param.put("nid", "LetteronCommissioner");//角色标示
        List<SysUser> users = sysUserService.getUserName(param);
        result.put("artificialList", users);
        //分配人
        result.put("userExamineList", userExamineService.listUserExamineInfo1());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @description
     * 1.人工复审查询(需要重构,需要初步调试通过..)
     * 2.我的信审订单,必须传入特殊字段 sysUserId
     *
     * @param searchParams
     * @param current
     * @param pageSize     人工复审 modules/manage/borrow/reviewList.htm
     * @date : 2017-12-19 16:38:22
     * @author: nmnl
     * @throws Exception
     */
    @RequestMapping(value = "reviewBorrowList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> reviewList(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                  @RequestParam(value = "current") Integer current,
                                                  @RequestParam(value = "pageSize") Integer pageSize) throws Exception{
        //查询条件
        Map<String, Object>
                params = parseToMap(searchParams,true);
        //订单状态
        List<String> stateList = new ArrayList<>();
        if (null != params && params.size() > 0) {
            String state = String.valueOf(params.get("state"));
            if (null != BorrowEnum.BORROW_STATE.getByEnumKey(state)) {
                stateList.add(state);
            }
        }else{
            for (BorrowEnum.BORROW_STATE mbEnum : BorrowEnum.BORROW_STATE.values()){
                stateList.add(mbEnum.EnumKey());
            }
        }
        //我的信审订单
        if (null != params.get("sysUserId")){
            SysUser sysUser = getLoginUser();
            params.put("sysUserId",sysUser.getId());
        }
        //人工复审分配人ID，allotSysUserId和sysUserId区分
        if (null != params.get("allotSysUserId")){
            params.put("sysUserId",params.get("allotSysUserId"));
        }
        params.remove("state");
        params.put("stateList",stateList);
        Page<BorrowMainModel> borrowMainPage = (Page<BorrowMainModel>)borrowMainService.selectBorrowList(params, current, pageSize);
        //审核记录
        for (BorrowMainModel page : borrowMainPage){
            BorrowAuditLogWithBLOBs data = borrowAuditLogService.findLogs(page.getId());
            if(null != data){
                page.setAuditData(data.getAuditData());
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, borrowMainPage);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(borrowMainPage));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @description 查询启用中的信审专员
     *
     * @throws Exception
     * @author: nmnl
     * @date : 2018-01-03 16:54
     * /modules/manage/borrow/allotmanList.htm
     */
    @RequestMapping(value = "searchAllotManList.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> searchallotmanList() throws Exception {
        Map<String, Object> result = new HashMap<>();
        //启用中的信审专员
        result.put("allotManList",userExamineService.selectAllotMan());
         return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @description 订单分配.
     * @param borrowMainIds
     * @param userId
     * @param userName
     * @date : 2018-01-03 17:44
     * @author: nmnl
     * @throws Exception
     * 人工复审 /modules/manage/borrow/allotBorrowOrder.htm
     */
    @RequestMapping(value = "allotBorrowOrder.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> allotBorrowOrder(@RequestParam(value = "borrowMainIds") Long [] borrowMainIds,
                                                        @RequestParam(value = "userId") Long userId,
                                                        @RequestParam(value = "userName") String userName) throws Exception{
        return new ResponseEntity<>(ResultModel.ok(borrowMainService.orderAllotSysUsers(getLoginUser(),borrowMainIds,userId,userName)), HttpStatus.OK);
    }

    /**
     * （风控下所有字典项）
     * @description 查询所有下拉条件
     *
     * @throws Exception
     * @author: nmnl
     * @date : 2017-12-28 14:15
     */
    @RequestMapping(value = "searchDictionaryList.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> searchDictionaryList(String searchType) throws Exception {
        Map<String, Object> result = new HashMap<>();
        if(null == RiskEnum.DICTIONARY_TYPE.getByEnumKey(searchType)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        //复审字典
        if(searchType.equals(RiskEnum.DICTIONARY_TYPE.ONE.EnumKey())){
            //复审类型
            result.put("auditTypeList", RiskEnum.AUDITING_TYPE.EnumValueS());
            //复审状态
            result.put("auditStateList", RiskEnum.AUDITING_STATE.EnumValueS());
        }else if(searchType.equals(RiskEnum.DICTIONARY_TYPE.TWO.EnumKey())){
            //通讯字典类型
            result.put("contactTypeList", RiskEnum.CONTACT_TYPE.EnumValueS());
        }else if(searchType.equals(RiskEnum.DICTIONARY_TYPE.FOUR.EnumKey())){
            //省份字典状态
            result.put("contactTypeList", RiskEnum.PROVINCE_STATE.EnumValueS());
        }
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @description 复审(意见)备注:列表查询
     *
     * @param searchParams
     * @param current
     * @param pageSize     信息字典列表 /modules/manage/examine/orderRefuseCause.htm
     * @date : 2017-12-20 15:14:22
     * @author: nmnl
     * @throws Exception
     */
    @RequestMapping(value = "orderRefuseCause.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> orderRefuseCause(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                        @RequestParam(value = "current") Integer current,
                                                        @RequestParam(value = "pageSize") Integer pageSize)throws Exception {
        //查询条件
        Map<String, Object> params = parseToMap(searchParams,true);
        Page<ExamineDictDetail> page = userExamineService.selectExamineDictDetailInfo(params, current, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @description 复审(意见)备注:新增/修改
     *
     * @param exma /modules/manage/examine/saveRefuseCause.htm
     * @date : 2017-12-20 15:14:22
     * @author: nmnl
     * @throws Exception
     */
    @RequestMapping(value = "saveOrUpdRefuseCause.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> saveOrUpdRefuseCause(ExamineDictDetail exma)throws Exception {
        //获取当前登录用户
        AuthUserRole authUserRole = getAuthUserRole();
        if(null != exma.getId()){
            userExamineService.updateExamineDictDetail(exma.getId(),authUserRole.getRealName(),exma.getValue());
        }else{
            exma.setCreator(authUserRole.getRealName());
            userExamineService.saveExamineDictDetail(exma);
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }


    /**
     * @description 复审(意见)备注:禁用/启用
     *
     * @param id /modules/manage/examine/deleteRefuseCause.htm
     * @date : 2017-12-20 15:14:22
     * @author: nmnl
     */
    @RequestMapping(value = "updateRefuseCauseState.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> updateRefuseCauseState(@RequestParam(value = "id", required = false) Long id) {
        //获取当前登录用户
        AuthUserRole authUserRole = getAuthUserRole();
        return new ResponseEntity<>(ResultModel.ok(userExamineService.updateExamineDictDetail(id,authUserRole.getRealName(),null)), HttpStatus.OK);
    }

    /**
     * @description 通讯录字典库:列表
     *
     * @param current
     * @param pageSize
     * @param searchParams
     * @throws Exception /modules/manage/contact/dict/list.htm
     * @date : 2017-12-20 15:14:22
     * @author: nmnl
     */
    @RequestMapping(value = "listContactDict.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> listContactDict(@RequestParam(value = "current") Integer current,
                                                       @RequestParam(value = "pageSize") Integer pageSize,
                                                       @RequestParam(value = "searchParams", required = false) String searchParams) throws Exception {
        //查询条件
        Map<String, Object> params = parseToMap(searchParams,true);
        Page<UserContactsMatchDict> page = userContactsMatchDictService.list(current, pageSize, params);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }


    /**
     * @description 通讯录字典库: 保存/更新
     *
     * @param contactMatchDict /modules/manage/contact/dict/save.htm
     *                         /modules/manage/contact/dict/update.htm
     * @throws Exception
     * @date : 2017-12-20 15:14:22
     * @author: nmnl
     */
    @RequestMapping(value = "saveOrUpdContactDict.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> saveOrUpdContactDict(UserContactsMatchDict contactMatchDict) throws Exception {
        if (null != contactMatchDict) {
            if (null != contactMatchDict.getId()) {
                userContactsMatchDictService.update(contactMatchDict);
            } else {
                userContactsMatchDictService.save(contactMatchDict);
            }
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * @description 通讯录字典库: 删除字典
     *
     * @param /modules/manage/idnoProvince/dict/del.htm
     * @date : 2018-1-16 15:14:22
     * @throws Exception
     */
    @RequestMapping(value = "delContactDict.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> delContactDict(@RequestParam(value = "id")Long id) throws Exception{
        return new ResponseEntity<>(ResultModel.ok(userContactsMatchDictService.delById(id)), HttpStatus.OK);
    }


    /**
     * @description 催收电话号码库:查询
     * @param current
     * @param pageSize
     * @param searchParams modules/manage/overphone/list.htm
     * @date : 2017-12-20 15:14:22
     * @author: nmnl
     * @throws Exception
     */
    @RequestMapping(value = "listOverduePhone.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> listOverduePhone(@RequestParam(value = "current") Integer current,
                                                        @RequestParam(value = "pageSize") Integer pageSize,
                                                        @RequestParam(value = "searchParams", required = false) String searchParams
    ) throws Exception{
        //查询条件
        Map<String, Object> params = parseToMap(searchParams,true);
        Page<OverduePhone> page = overduePhoneService.list(current, pageSize, params);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @description 催收电话号码库:保存/更新
     * @param overduePhone /modules/manage/overphone/save.htm
     * @date : 2017-12-20 15:14:22
     * @author: nmnl
     */
    @RequestMapping(value = "saveOrUpdOverduePhone.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> saveOrUpdOverduePhone(OverduePhone overduePhone) {
        //获取当前登录用户
        AuthUserRole authUserRole = getAuthUserRole();
        if (null != overduePhone) {
            if (null != overduePhone.getId()) {
                overduePhoneService.update(overduePhone);
            } else {
                overduePhone.setCreateUserid(authUserRole.getUserId());
                overduePhoneService.save(overduePhone);
            }
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * @description 催收电话号码库:删除
     * @param ids modules/manage/overphone/delete.htm
     * @date : 2017-12-20 15:14:22
     * @author: nmnl
     */
    @RequestMapping(value = "delOverduePhone.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> delOverduePhone(Long ... ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            overduePhoneService.deleteByIds(Arrays.asList(ids));
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * @description 省份字典库:查询
     * @throws Exception
     * @date : 2017-12-21 15:14:22
     * @author: nmnl
     * /modules/manage/idnoProvince/dict/list.htm
     * @throws Exception
     */
    @RequestMapping(value = "listIdnoProvinceDict.htm",method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> listIdnoProvinceDict(@RequestParam(value = "current") Integer current,
                                                            @RequestParam(value = "pageSize") Integer pageSize,
                                                            @RequestParam(value = "searchParams", required = false) String searchParams) throws Exception{
        //查询条件
        Map<String, Object> params = parseToMap(searchParams,true);
        Page<UserIdnoMatchDict> page = userIdnoMatchDictService.list(current,pageSize,params);
        Map<String,Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @description 省份字典库:新增/更新
     * @param userIdnoMatchDict
     * @throws Exception
     * @date : 2017-12-21 15:14:22
     * @author: nmnl
     * /modules/manage/idnoProvince/dict/updateProvince.htm
     * /modules/manage/idnoProvince/dict/save.htm
     */
    @RequestMapping(value = "saveOrUpdIdnoProvinceDict.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> saveOrUpdIdnoProvinceDict(UserIdnoMatchDict userIdnoMatchDict) throws Exception{
        if (null == userIdnoMatchDict.getId()){
            userIdnoMatchDictService.save(userIdnoMatchDict);
        } else {
            userIdnoMatchDictService.update(userIdnoMatchDict);
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * @description 省份字典库:启用／停用
     * @param id
     * @throws Exception
     * @date : 2017-12-21 15:14:22
     * @author: nmnl
     * /modules/manage/idnoProvince/dict/updateState.htm
     */
    @RequestMapping(value = "updateIdnoProvinceDictState.htm",method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> updateIdnoProvinceDictState(@RequestParam(value = "id")Long id) throws Exception{
        return new ResponseEntity<>(ResultModel.ok(userIdnoMatchDictService.updateState(id)), HttpStatus.OK);
    }


    /**
     * @description 信审人员 : 查询所有下拉条件
     * @author: huangqin
     * @date : 2018-01-01 22:42
     * @throws Exception
     */
    @RequestMapping(value = "userExamineDropDownList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> userExamineDropDownList() throws Exception {
        Map<String, Object> result = new HashMap<>();
        //信审人员状态
        result.put("stateList", ExamineEnum.EXAMINE_STATE.EnumValueS());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @description 信审人员 : 信审人员查询
     * @param searchParams
     * @param current
     * @param pageSize
     * @throws Exception
     * @author: nmnl
     * @date : 2018-01-02 11:14
     */
    @RequestMapping(value = "userExamineList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> userExamineList(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                       @RequestParam(value = "current") Integer current,
                                                       @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        Map<String, Object> params = parseToMap(searchParams, true);
        Map<String, Object> result = new HashMap<>();
        Page<UserExamine> page = userExamineService.listUserExamineInfo(params, current, pageSize);
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @description 信审人员 : 批量添加信审人员,查询!
     * @author: nmnl
     * @date : 2018-01-02 11:14
     * @throws Exception
     */
    @RequestMapping(value = "searchUnselectedExamine.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> searchUnselectedExamine() throws Exception {
        return new ResponseEntity<>(ResultModel.ok(userExamineService.listadduser()), HttpStatus.OK);
    }

    /**
     * @description 信审人员 : 添加信审人员
     * @param userIds
     * @author: nmnl
     * @date : 2018-01-02 11:14
     * @throws Exception
     */
    @RequestMapping(value = "examineSave.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> examineSave(@RequestParam(value = "userIds")Long ... userIds) throws Exception {
        if (null == userIds || userIds.length <= 0) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        userExamineService.addUserExamInfo(userIds);
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }
}