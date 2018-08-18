package com.adpanshi.cashloan.manage.controller;

import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.data.common.enums.ChannelBizType;
import com.adpanshi.cashloan.data.common.enums.ChannelType;
import com.adpanshi.cashloan.manage.arc.model.SysDict;
import com.adpanshi.cashloan.manage.arc.model.SysDictDetail;
import com.adpanshi.cashloan.manage.arc.service.SysDictDetailService;
import com.adpanshi.cashloan.manage.arc.service.SysDictService;
import com.adpanshi.cashloan.manage.cl.enums.BorrowEnum;
import com.adpanshi.cashloan.manage.cl.model.*;
import com.adpanshi.cashloan.manage.cl.model.expand.*;
import com.adpanshi.cashloan.manage.cl.model.moxie.*;
import com.adpanshi.cashloan.manage.cl.pojo.CallRecord;
import com.adpanshi.cashloan.manage.cl.pojo.equifaxReport.Envelope;
import com.adpanshi.cashloan.manage.cl.service.*;
import com.adpanshi.cashloan.manage.core.common.cache.RedissonClientUtil;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import com.adpanshi.cashloan.manage.core.common.util.RdPage;
import com.adpanshi.cashloan.manage.core.common.util.StringUtil;
import com.adpanshi.cashloan.manage.pojo.ResultModel;

import com.adpanshi.cashloan.manage.report.domain.AuditDomain;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.adpanshi.cashloan.manage.report.bo.ManageReportResponseBo;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringUtils;
import org.redisson.core.RLock;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * (围绕用户画像) Controller
 * 0.规则信息
 * 0.1 暂时提供新用户信息.
 * 1.基本信息
 * 1.1 用户基本信息.包涵所有用户信息
 * 1.2 有盾ocr,活体
 * 1.4 各种认证状态
 * 2.通讯录
 * 2.1 app抓去用户手机的通讯录
 * 3.运营商通话记录
 * 3.1 近6个月的通话记录
 * 4.借款记录
 * 4.1 用户在该平台借款的记录
 * 4.2 借款详情,合同过程..
 * 7.已装应用
 * 7.1 app抓去用户手机的app信息
 * 10.催收反馈
 * 10.1 催收反馈记录
 *
 * @version 2.0.0
 * @date 2017-12-21 21:39
 * @author: nmnl
 * Copyright 粉团网路 arc All Rights Reserved
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/loanother/")
public class ManageLoanOtherController extends ManageBaseController {
    @Resource
    private ChannelService channelService;
    @Resource
    private UserBaseInfoService userBaseInfoService;
    @Resource
    private UserService userService;
    @Resource
    private UserAuthService authService;
    @Resource
    private BankCardService bankCardService;
    @Resource
    private UserEmerContactsService userEmerContactsService;
    @Resource
    private BorrowService borrowService;
    @Resource
    private BorrowAuditLogService borrowAuditLogService;
    @Resource
    private UserExamineService userExamineService;
    @Resource
    private UrgeRepayOrderService urgeRepayOrderService;
    @Resource
    private BorrowMainService borrowMainService;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private SysDictDetailService sysDictDetailService;
    @Resource
    private UrgeRepayLogService urgeRepayLogService;
    @Resource
    private AuditDomain auditDomain;
    @Resource
    private OperatorReqLogService operatorReqLogService;


    /**
     * 用户管理:详情
     * 人工复审(复审管理):详情
     * 1. 借款:选择审核意见
     * /modules/manage/borrow/orderRefuseCause.htm
     * 借款管理:详情
     * 贷后管理:详情
     * 催收管理:详情
     * <p>
     * 思路:
     * 1.人工复审,增加操作查看记录
     * 2.催收订单,增加操作记录.谁操作过..
     * 最终思路:
     * 1.根据当前info.htm 获取所有 用户管理可以访问的url. 二次赋权 * (时间方面,暂时不做补充.)
     * 2.数据填充铺垫,比如审核意见.
     *
     * @param borrowMainId
     * @throws Exception
     * @author: nmnl
     * @date: 2017-12-22 14:50
     */
    @RequestMapping(value = "info.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> info(Long borrowMainId, String infoType) throws Exception {
        Map<String, Object> result = new HashMap<>();
        AuthUserRole authUserRole = getAuthUserRole();
        if (null == borrowMainId || null == BorrowEnum.LOAN_INFO_TYPE.getByEnumKey(infoType) || null == authUserRole) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        result.put("borrowMain",borrowMainService.getById(borrowMainId));
        //借款管理插入人工复审记录
        if (BorrowEnum.LOAN_INFO_TYPE.REVIEW_MANAGE_EDIT.EnumKey().equals(infoType)) {
            //需要判断是否需要审核.
            borrowAuditLogService.addLog(borrowMainId, authUserRole.getUserId(), authUserRole.getRealName());
            //审核意见类型.
            result.put("allExamineTypeList", BorrowEnum.AUDITING_BORROW_STATE.EnumValueS());
            //审核意见信息
            result.put("allExamineList", userExamineService.selectBorrowExamineDictDetailInfo(null));
        }

        if (BorrowEnum.LOAN_INFO_TYPE.REVIEW_MANAGE_INFO.EnumKey().equals(infoType)) {
            result.put("borrowAuditLog",borrowAuditLogService.findLogs(borrowMainId));
        }
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }


    /**
     * 后台人工复审功能(严重重构)
     *
     * @param borrowMainId 主订单
     * @param state 审核状态
     * @param orderView 审核意见.
     * @param remark 订单备注.
     * @throws Exception /modules/manage/borrow/verifyBorrow.htm
     * @author: nmnl
     * @date: 2017-12-30 17:19
     */
    @RequestMapping(value = "verifyBorrow.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> verifyBorrow(@RequestParam(value = "borrowMainId") Long borrowMainId, @RequestParam(value = "state") String state,
                                                    @RequestParam(value = "remark") String remark, @RequestParam(value = "orderView") String orderView) throws Exception {
        if (null == borrowMainId || null == BorrowEnum.BORROW_STATE.getByEnumKey(state)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        //redis锁
        RLock rlock = RedissonClientUtil.getInstance().getLock("verifyBorrow_" + borrowMainId);
        try {
            rlock.lock(50, TimeUnit.SECONDS);
            AuthUserRole user = getAuthUserRole();
            String resultState = borrowMainService.manualVerifyBorrow(borrowMainId, state, remark, user.getUserId(), user
                    .getRealName(), orderView);
            Boolean isSuccess;
            if(StringUtils.isNotEmpty(resultState)){
                isSuccess = true;
            }else {
                isSuccess = false;
            }
            return new ResponseEntity<>(ResultModel.ok(isSuccess), HttpStatus.OK);
        } finally {
            rlock.unlock();
        }
    }

//    /**
//     * 借款:规则详情
//     *
//     * @param borrowMainId
//     * @throws Exception
//     * @author: nmnl
//     * @date: 2017-12-22 10:14
//     * /modules/manage/review/findResult.htm
//     */
//    @RequestMapping(value = "borrowFindResult.htm", method = {RequestMethod.POST})
//    public ResponseEntity<ResultModel> findResult(@RequestParam(value = "borrowMainId") Long borrowMainId) throws Exception {
//        if (null == borrowMainId) {
//            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(ResultModel.ok(BorrowService.findResult(borrowMainId)), HttpStatus.OK);
//    }

    /**
     * 用户:详细信息
     *
     * @param userId
     * @author: nmnl
     * @date: 2017-12-16
     * @version: 2.0.0
     * /manage/cl/cluser/detail.htm
     */
    @RequestMapping(value = "userDetail.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> userDetail(@RequestParam(value = "userId") Long userId) {
        //读库标志DatabaseContextHolder.setDbName(DatabaseContextHolder.READ_DB);
        //查询条件
        if (null == userId || 0 == userId) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        //服务器域名||IP
        String serverHost = "/manage/export/getImg.htm?path=";
        HashMap<String, Object> map = new HashMap<>(16);
        User user = userService.getById(userId);
        if (user != null && user.getId() != null) {
            //用户基本信息-> 画像
            UserBaseInfoModel model = userBaseInfoService.getBaseModelByUserId(userId);
            model.setLivingImg(model.getLivingImg() != null ? serverHost + model.getLivingImg() : "");
            model.setFrontImg(model.getFrontImg() != null ? serverHost + model.getFrontImg() : "");
            model.setBackImg(model.getBackImg() != null ? serverHost + model.getBackImg() : "");
            model.setOcrImg(model.getOcrImg() != null ? serverHost + model.getOcrImg() : "");

            if (StringUtils.isNotBlank(model.getWorkingImg())) {
                String workImgStr = model.getWorkingImg();
                List<String> workImgList = Arrays.asList(workImgStr.split(";"));
                for (int i = 0; i < workImgList.size(); i++) {
                    String workImg = workImgList.get(i);
                    workImgList.set(i, serverHost + workImg);
                }
                map.put("workImgArr", workImgList);
            }

            //银行卡信息
            BankCard bankCard = bankCardService.getBankCardByUserId(user.getId());
            if (null != bankCard) {
                model.setBank(bankCard.getBank());
                model.setCardNo(bankCard.getCardNo());
                model.setBankPhone(bankCard.getPhone());
                model.setIfscCode(bankCard.getIfscCode());
            }

            Channel cl = channelService.getById(user.getChannelId());
            if (cl != null) {
                model.setChannelName(cl.getName());
            }

            if (null != model){
                map.put("userbase", model);
            }

            // 构造查询条件Map
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", userId);

            // 认证信息
            UserAuth authModel = authService.getUserAuthByUserId(userId);
            if (null != authModel){
                map.put("userAuth", authModel);
            }

            // 联系人信息
            List<UserEmerContacts> infoModel = userEmerContactsService.getUserEmerContactsByUserId(userId);
            if (null != infoModel && infoModel.size() > 0) {
                map.put("userContactInfo", infoModel);
            }
        }
        return new ResponseEntity<>(ResultModel.ok(map), HttpStatus.OK);
    }

    /**
     * 用户:通讯录查询(做了字典匹配)
     *
     * @param userId
     * @param current
     * @param pageSize
     * @author: nmnl
     * @date: 2017-12-16
     */
    @RequestMapping(value = "userContactsList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> userContactsList(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "current") Integer current,
            @RequestParam(value = "pageSize") Integer pageSize,
             String phone) {
        Map<String, Object> result = new HashMap<>();
        if (null == userId || null == current || null == pageSize) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        UserBaseInfo userBaseInfo = userBaseInfoService.getBaseModelByUserId(userId);
        if(userBaseInfo.getUserDataId() == null){
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }else{
            Integer userDataId = userBaseInfo.getUserDataId().intValue();
//            String resultJson = "{\\\"userId\\\":\\\"161\\\",\\\"info\\\":[{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"房东\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"哥\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"咖啡厅宝珠\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"胡小燕\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"黄老师\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"焦小娟\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"凯丽\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"老爸\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"刘加友\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"刘云\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"罗国群\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"妈\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"妈\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"单素玲\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"王蓉\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"王晓羽\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"晏丽梅\\\"},{\\\"phone\\\":\\\"\\\",\\\"name\\\":\\\"杨晓倩\\\"}]}";
            String resultJson = (String )auditDomain.getAuditInfo(userDataId,ChannelType.PSAPP,ChannelBizType.APP_COMMUNICATION).getData();
            if(resultJson != null){
                JSONArray array= JSONArray.parseArray(resultJson);
                RdPage page = new RdPage();
                List<UserContactsModel> realData = array.toJavaList(UserContactsModel.class);
                //根据phone筛选通讯录数据
                if(phone != null &&realData.size() >0){
                    Iterator<UserContactsModel> it = realData.iterator();
                    while(it.hasNext()){
                        UserContactsModel model = it.next();
                        model.setState(0);
                        if(!model.getPhone().contains(phone)){
                            it.remove();
                        }
                    }
                }
                List<UserEmerContacts> emerContacts = userEmerContactsService.getUserEmerContactsByUserId(userId);
                if(realData.size() > 0 && emerContacts.size() > 0){
                    //判断是否匹配紧急联系人
                    for(int i = 0;i < emerContacts.size();i++){
                        for(int j = 0;j < realData.size();j++){
                            logger.info(realData.get(j).getPhone());
                            System.out.println(realData.get(j).getPhone());
                            if(realData.get(j).getPhone().contains(emerContacts.get(i).getPhone())){
                                realData.get(j).setState(1);
                            }
                        }
                    }
                }
                //集合按匹配度降序排序
                realData.sort(Comparator.comparing(UserContactsModel::getState).reversed());
                List<UserContactsModel> data = listToPage(realData,page,current,pageSize);
                result.put(Constant.RESPONSE_DATA, data);
                result.put(Constant.RESPONSE_DATA_PAGE, page);
            } else {
                RdPage page = new RdPage();
                page.setTotal(0);
                page.setPageSize(pageSize);
                page.setCurrent(current);
                page.setPages(0);
                result.put(Constant.RESPONSE_DATA, new ArrayList<>());
                result.put(Constant.RESPONSE_DATA_PAGE, page);
            }
            return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
        }
    }



    /**
     * 借款:借款记录(主订单)
     *
     * @param userId
     * @param current
     * @param pageSize
     * @throws Exception
     * @author: nmnl
     * @date: 2017-12-22 10:48
     * /modules/manage/borrow/listBorrowLogForPersion.htm
     */
    @RequestMapping(value = "borrowLogList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> borrowLogList(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "current") Integer current,
            @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        if (null == userId || null == current || null == pageSize) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        //主订单+子订单(分期计划)
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",userId);
        Page<BorrowModel> page = borrowService.listBorrowModel(map,current,pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 用户:app所有列表
     *
     * @param userId
     * @param current
     * @param pageSize
     * @throws Exception
     * @author: nmnl
     * @date : 2017-12-16 16:50:33
     */
    @RequestMapping(value = "userAppList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> userAppList(@RequestParam(value = "current") Integer current,
                                                   @RequestParam(value = "pageSize") Integer pageSize,
                                                   @RequestParam(value = "userId") Long userId) throws Exception {
        Map<String, Object> result = new HashMap<>();
        if (null == userId || null == current || null == pageSize) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        UserBaseInfo userBaseInfo = userBaseInfoService.getBaseModelByUserId(userId);
        if(userBaseInfo.getUserDataId() == null){
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }else{
            Integer userDataId = userBaseInfo.getUserDataId().intValue();
            int count = 0;
            String resultJson = (String)auditDomain.getAuditInfo(userDataId, ChannelType.PSAPP, ChannelBizType.APP_APPLICATION).getData();
            if(resultJson != null){
                JSONArray array= JSONArray.parseArray(resultJson);
                RdPage page = new RdPage();
                List<UserAppsModel> realData = array.toJavaList(UserAppsModel.class);
                for(int i = 0;i < realData.size();i++){
                    realData.get(i).setStatus(0);
                    if(Constant.LOAN_APP_NAME.contains(realData.get(i).getAppName()) ||
                            Constant.LOAN_APP_PACKAGE_NAME.contains(realData.get(i).getPackageName())){
                        realData.get(i).setStatus(1);
                        count ++;
                    }
                }
                realData.sort(Comparator.comparing(UserAppsModel::getStatus).reversed());
                List<UserApps> data = listToPage(realData,page,current,pageSize);
                result.put(Constant.RESPONSE_DATA, data);
                result.put("count", count);
                result.put(Constant.RESPONSE_DATA_PAGE, page);
            } else {
                RdPage page = new RdPage();
                page.setTotal(0);
                page.setPageSize(pageSize);
                page.setCurrent(current);
                page.setPages(0);
                result.put(Constant.RESPONSE_DATA, new ArrayList<>());
                result.put("count", count);
                result.put(Constant.RESPONSE_DATA_PAGE, page);
            }
            return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
        }
    }

//    /**
//     * 借款:重新发起风控审核
//     *
//     * @param borrowMainId
//     * @author: nmnl
//     * @date : 2017-12-22 11:51
//     * /modules/manage/borrow/reVerifyBorrowDataForRisk.htm
//     */
//    @RequestMapping(value = "borrowReVerifyForRisk.htm", method = RequestMethod.POST)
//    public ResponseEntity<ResultModel> borrowReVerifyForRisk(@RequestParam(value = "borrowMainId") Long borrowMainId) {
//        //redis锁
//        RLock rlock = RedissonClientUtil.getInstance().getLock("borrowReVerifyForRisk_" + borrowMainId);
//        try {
//            rlock.lock(50, TimeUnit.SECONDS);
//            return new ResponseEntity<>(ResultModel.ok(borrowFacade.reVerifyBorrowDataForRisk(borrowMainId)), HttpStatus.OK);
//        } finally {
//            rlock.unlock();
//        }
//    }



    /**
     * 催收处理:催收反馈:获取字典详情列表
     *
     * @param current
     * @param pageSize
     * @param code
     * @throws Exception
     * @author: huangqin
     * @date : 2018-01-01 15:39
     */
    @RequestMapping(value = "collFindDict.htm")
    public ResponseEntity<ResultModel> findByTypeCode(@RequestParam(value = "current") Integer current,
                                                      @RequestParam(value = "pageSize") Integer pageSize,
                                                      @RequestParam(value = "code") String code) throws Exception {
        SysDict dict = sysDictService.findByTypeCode(code);
        Map<String, Object> result = new HashMap<>(16);
        if (null == dict) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_VALUE), HttpStatus.OK);
        }
        Page<SysDictDetail> page = sysDictDetailService.getDictDetailList(current, pageSize, dict.getId());
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 催收处理:催收反馈列表查询
     *
     * @param current
     * @param pageSize
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     * @author: huangqin
     * @date : 2018-01-01 15:39
     */
    @RequestMapping(value = "logListbyBorrowId.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> logListByBorrowId(@RequestParam(value = "current") Integer current,
                                                         @RequestParam(value = "pageSize") Integer pageSize,
                                                         @RequestParam(value = "borrowId") Long borrowId,
                                                         @RequestParam(value = "isAdmin", required = false) Boolean isAdmin) {
        Map<String, Object> params = new HashMap<>();
        if(null == isAdmin || !isAdmin){
            params.put("logUserId", getLoginUser().getId());
        }
        params.put("borrowId", borrowId);
        Page<UrgeRepayOrderModel> page = urgeRepayOrderService.listModel(params, current, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 催收处理:催收反馈-添加催款反馈信息
     *
     * @param urgeRepayLog
     * @return ResponseEntity<ResultModel>
     * @author: huangqin
     * @date : 2018-01-01 15:39
     */
    @RequestMapping(value = "saveOrderInfo.htm", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<ResultModel> saveOrderInfo(UrgeRepayLog urgeRepayLog) {
        if (null == urgeRepayLog || null == urgeRepayLog.getDueId() || urgeRepayLog.getDueId().compareTo(0L) <= 0) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        UrgeRepayOrder order = urgeRepayOrderService.getById(urgeRepayLog.getDueId());
        //提交信息有误
        if (null == order) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MANAGE_COLL_PUSHDATA_ERROR_CODE_VALUE), HttpStatus.OK);
        }
        urgeRepayLogService.saveOrderInfo(urgeRepayLog, order);
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }
    /**
     * 获取信用报告信息
     *
     * @param userId
     * @return ResponseEntity<ResultModel>
     * @author: tangqiang
     * @date : 2018-07-16
     */
    @RequestMapping(value = "getEquifaxReportDetail.htm", method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<ResultModel> getEquifaxReportDetail(@RequestParam(value = "userId") Long  userId) {
        Integer userDataId = userBaseInfoService.getBaseModelByUserId(userId).getUserDataId().intValue();
        String resultXml = (String) auditDomain.getAuditInfo(userDataId, ChannelType.EQUIFAXREPORT, ChannelBizType.EQUIFAX_REPORT_ANALYZE).getData();
        StringBuffer sb = new StringBuffer();
        sb.append("<soapenv:Envelope>");
        sb.append("<soapenv:Body>");
        sb.append("<sch:InquiryResponse>");
        try {
            int i = resultXml.indexOf("<sch:InquiryResponseHeader>");
            resultXml = resultXml.substring(i, resultXml.length());
            XStream xs = new XStream();
            //xml节点对应实体类
            xs.alias("Envelope", Envelope.class);
            xs.processAnnotations(new Class[]{Envelope.class});
            xs.ignoreUnknownElements();
            sb.append(resultXml);
            //通过这种方式把xml转成对象
            Object obj = xs.fromXML(sb.toString());
            //强转成Data 对象
            Envelope data = (Envelope) obj;
            return new ResponseEntity<>(ResultModel.ok(data), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
        }
    }

    /**
     * 借款:借款记录详情
     * 1.关联场景(租房合同及收入证明)
     * 2.还款计划
     *
     * @param userId
     * @param borrowMainId
     * @throws Exception /modules/manage/borrow/borDetailBySceneWithAttachment.htm
     *                   /modules/manage/borrow/progress/list.htm
     */
    @RequestMapping(value = "borrowSceneProgress.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> borrowSceneProgress(@RequestParam(value = "userId") Long userId,
                                                           @RequestParam(value = "borrowMainId") Long borrowMainId) throws Exception {
        Map<String, Object> result = new HashMap<>();
        if (null == userId || null == borrowMainId) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        //还款计划 ->
        BorrowMain borrowMain = borrowMainService.getById(borrowMainId);
        if (null != borrowMain) {
            result.put("borrowMain", borrowMain);
        }
        //数据割接. 割接原因:旧数据和新数据,旧数据中模版信息是没值
        result.put("borrowMainId",borrowMainId);
        result.put("borrowMainPlan",borrowService.findBorrowByMap(result));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 数据魔盒:获取用户手机SIM卡信息/FaceBook信息
     * 根据用户userID一次性检索出全部数据，如果要查看部分需要加条件
     * @param userId
     * @throws Exception
     * @author: 8470
     * @date : 2018-07-23 11:55:03
     */
    @RequestMapping(value = "getSimListRecords.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> getSimListRecords(@RequestParam(value = "userId") Long userId) throws Exception {
        Map<String, Object> result = new HashMap<>();
        if (null == userId) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }

        //根据userId获取userBaseInfo信息
        UserBaseInfoModel userBaseInfoModel = userBaseInfoService.getBaseModelByUserId(userId);
        String id = String.valueOf(userBaseInfoModel.getUserDataId());
        ManageReportResponseBo manageReportResponseBo = auditDomain.getAuditInfo(Integer.valueOf(id), ChannelType.MOXIESIMCARD, ChannelBizType.MOXIE_SIM_CARD_INFO);
        if(!StringUtil.isEmpty(manageReportResponseBo.getData())){
            //获取SIM卡返回数据并解析
            String metaData = String.valueOf(manageReportResponseBo.getData());
            //将原始数据转换为map
            Map<String,Object> metaDataMap = JSON.parseObject(metaData,Map.class);
            //初始化短信/通话记录
            List<MessageBill> messageBillList = new ArrayList<>();
            List<CallingRecord> callingRecordList = new ArrayList<>();

            if("0".equals(String.valueOf(metaDataMap.get("code")))){
                //获取data数据
                String data = String.valueOf(metaDataMap.get("data"));
                Map<String,Object> dataMap = JSON.parseObject(data,Map.class);
                String taskData = String.valueOf(dataMap.get("task_data"));
                //获取原始数据
                Map<String,Object> taskDataMap = JSON.parseObject(taskData,Map.class);
                //获取原始数据中的短信
                String smsData = String.valueOf(taskDataMap.get("sms"));
                messageBillList = JSON.parseArray(smsData,MessageBill.class);

                //获取原始数据中的通话
                String callData = String.valueOf(taskDataMap.get("call_records"));
                callingRecordList = JSON.parseArray(callData, CallingRecord.class);
            }
            result.put(Constant.RESPONSE_DATA, callingRecordList);
            result.put(Constant.RESPONSE_DATA_PAGE, messageBillList);
        }

        //查询FaceBook信息(暂时先考虑最新的一条，后期如果查询全部再修改)
        Map<String,Object> paramsReq = new HashMap<>();
        List<OperatorReqLog> operatorReqLogList = new ArrayList<>();
        paramsReq.clear();
        paramsReq.put("userId",userId);
        paramsReq.put("businessType","20");
        paramsReq.put("respCode","200");
        OperatorReqLog operatorReqLogs = operatorReqLogService.findLastRecord(paramsReq);
        if(operatorReqLogs!=null&& StringUtils.isNotEmpty(operatorReqLogs.getOrderNo())){
            //根据taskID获取FaceBook信息
            operatorReqLogList.add(operatorReqLogs);
            result.put("operatorReqLog", operatorReqLogList);
        }

        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }


    /**
     * 数据魔盒:获取用户手机SIM卡信息/FaceBook信息
     * 根据用户userID一次性检索出全部数据，如果要查看部分需要加条件
     * @param callDetail
     * @throws Exception
     * @author: 8470
     * @date : 2018-07-23 11:55:03
     */
    @RequestMapping(value = "getCallDetailRecords.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> getCallDetailRecords(@RequestParam(value = "callDetail") String callDetail) throws Exception {
        Map<String, Object> result = new HashMap<>();
        if (StringUtil.isEmpty(callDetail)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        //解析通话记录详情
        List<CallingDetail> callingDetailList = new ArrayList<>();
        callingDetailList = JSON.parseArray(callDetail, CallingDetail.class);
        result.put(Constant.RESPONSE_DATA, callingDetailList);
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 数据魔盒:获取用户FaceBook信息
     * @param userId
     * @throws Exception
     * @author: 8470
     * @date : 2018-07-23 11:55:03
     */
    @RequestMapping(value = "getFBDetailRecords.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> getFBDetailRecords(@RequestParam(value = "userId") Long userId, @RequestParam(value = "taskId") String taskId) throws Exception {
        Map<String, Object> result = new HashMap<>();
        if (!StringUtil.isNotEmptys(userId,taskId)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        //根据用户userId获取userBaseInfo信息
        UserBaseInfoModel userBaseInfoModel = userBaseInfoService.getBaseModelByUserId(userId);
        String id = String.valueOf(userBaseInfoModel.getUserDataId());
        //调用数据接口获取原始数据
        ManageReportResponseBo manageReportResponseBo = auditDomain.getAuditInfo(Integer.valueOf(id),ChannelType.MOXIESNS, ChannelBizType.MOXIE_SNS_INFO);

        if(!StringUtil.isEmpty(manageReportResponseBo.getData())){
            //获取SNS卡返回数据并解析
            String metaData = String.valueOf(manageReportResponseBo.getData());
            //将原始数据转换为map
            Map<String,Object> metaDataMap = JSON.parseObject(metaData,Map.class);
            //初始化用户基本信息
            List<FaceBookBaseInfo> faceBookBaseInfoList = new ArrayList<>();

            if("0".equals(String.valueOf(metaDataMap.get("code")))) {
                //获取data数据
                String data = String.valueOf(metaDataMap.get("data"));
                Map<String, Object> dataMap = JSON.parseObject(data, Map.class);
                String taskData = String.valueOf(dataMap.get("task_data"));
                if(!StringUtil.isEmpty(taskData)) {
                    //获取工作信息
                    JSONObject taskDataJson = JSON.parseObject(taskData);
                    String workInfoStr = taskDataJson.getString("work_info");
                    List<FaceBookWorkInfo> faceBookWorkInfoList = JSON.parseArray(workInfoStr, FaceBookWorkInfo.class);
                    //获取家庭信息
                    String familyInfoStr = taskDataJson.getString("family_info");
                    List<FaceBookFamilyInfo> faceBookFamilyInfoList = JSON.parseArray(familyInfoStr, FaceBookFamilyInfo.class);
                    //特殊符号[]去除
                    for(FaceBookFamilyInfo faceBookFamilyInfo:faceBookFamilyInfoList){
                        if(faceBookFamilyInfo.getName().contains("[") && faceBookFamilyInfo.getName().contains("]")){
                            String name = faceBookFamilyInfo.getName().replace("[","").replace("]","");
                            faceBookFamilyInfo.setName(name);
                        }
                        if(faceBookFamilyInfo.getRelationship().contains("[") && faceBookFamilyInfo.getRelationship().contains("]")){
                            String relationShip = faceBookFamilyInfo.getRelationship().replace("[","").replace("]","");
                            faceBookFamilyInfo.setRelationship(relationShip);
                        }
                    }

                    //获取支付信息
                    String paymensInfoStr = taskDataJson.getString("payments_info");
                    List<FaceBookPaymentsInfo> faceBookPaymentsInfoList = JSON.parseArray(paymensInfoStr, FaceBookPaymentsInfo.class);

                    //获取好友信息
                    String friendsInfoStr = taskDataJson.getString("friends_info");
                    List<FaceBookFriendsInfo> faceBookFriendsInfoList = JSON.parseArray(friendsInfoStr, FaceBookFriendsInfo.class);

                    //获取发布信息
                    String publishInfoStr = taskDataJson.getString("publish_info");
                    List<FaceBookPublishInfo> faceBookPublishInfoList = JSON.parseArray(publishInfoStr, FaceBookPublishInfo.class);

                    //获取登录信息
                    String historicalLoginInfoStr = taskDataJson.getString("historical_login_info");
                    List<FaceBookHistoricalLoginInfo> faceBookHistoricalLoginInfoList = JSON.parseArray(historicalLoginInfoStr, FaceBookHistoricalLoginInfo.class);

                    //获取教育信息
                    String educationInfoStr = taskDataJson.getString("education_info");
                    JSONObject educationInfoObject = JSON.parseObject(educationInfoStr);
                    //初始化教育信息集合
                    List<FaceBookEducationInfo> faceBookEducationInfoList = new ArrayList<>();

                    //获取大学信息
                    String collegeInfoStr = educationInfoObject.getString("college_info");
                    if (collegeInfoStr != null) {
                        List<Map> collegeInfoList = JSONObject.parseArray(collegeInfoStr, Map.class);
                        for (int i = 0; i < collegeInfoList.size(); i++) {
                            //初始化教育信息
                            FaceBookEducationInfo faceBookEducationInfo = new FaceBookEducationInfo();
                            //暂时只考虑有一所大学
                            faceBookEducationInfo.setCollegename(String.valueOf(collegeInfoList.get(i).get("college_name")));
                            faceBookEducationInfo.setCollegegraduation(String.valueOf(collegeInfoList.get(i).get("college_graduation")));
                            faceBookEducationInfoList.add(faceBookEducationInfo);
                        }
                    }

                    //获取高中信息
                    String highschoolInfoStr = educationInfoObject.getString("highschool_info");
                    if (highschoolInfoStr != null) {
                        List<Map> highschoolList = JSONObject.parseArray(highschoolInfoStr, Map.class);
                        for (int i = 0; i < highschoolList.size(); i++) {
                            //初始化教育信息
                            FaceBookEducationInfo faceBookEducationInfo = new FaceBookEducationInfo();
                            //暂时只考虑有一所高中
                            faceBookEducationInfo.setHighschoolname(String.valueOf(highschoolList.get(i).get("highschool_name")));
                            faceBookEducationInfo.setHighschoolgraduation(String.valueOf(highschoolList.get(i).get("highschool_graduation")));
                            faceBookEducationInfoList.add(faceBookEducationInfo);
                        }
                    }

                    //获取个人信息
                    String baseInfoStr = taskDataJson.getString("base_info");
                    Map<String, Object> taskDataMap = JSON.parseObject(baseInfoStr, Map.class);
                    FaceBookBaseInfo faceBookBaseInfo = new FaceBookBaseInfo();
                    //个人信息=用户名
                    faceBookBaseInfo.setNickname(String.valueOf(taskDataMap.get("nickname")));
                    //个人信息=现居地
                    faceBookBaseInfo.setCurrentcity(String.valueOf(taskDataMap.get("currentcity")));
                    //个人信息=家乡
                    faceBookBaseInfo.setHometown(String.valueOf(taskDataMap.get("hometown")));
                    //个人信息=出生月日
                    faceBookBaseInfo.setBirthdate(String.valueOf(taskDataMap.get("birthdate")));
                    //个人信息=邮箱
                    faceBookBaseInfo.setEmail(String.valueOf(taskDataMap.get("email")));
                    //个人信息=宗教趋向
                    faceBookBaseInfo.setReligiontrend(String.valueOf(taskDataMap.get("religion_trend")));
                    //个人信息=政治趋向
                    faceBookBaseInfo.setPoliticaltrend(String.valueOf(taskDataMap.get("political_trend")));
                    //个人信息=生活状态
                    faceBookBaseInfo.setRelationshipstatus(String.valueOf(taskDataMap.get("relationshipstatus")));
                    //个人信息=好友数量
                    faceBookBaseInfo.setFriendsnum(Integer.valueOf(String.valueOf(taskDataMap.get("friendsnum"))));
                    //获取手机号码返回数组
                    if (taskDataMap.get("mobilephones") != null) {
                        List<String> mobilephones = JSONObject.parseArray(String.valueOf(taskDataMap.get("mobilephones")), String.class);
                        //个人信息=手机号码
                        faceBookBaseInfo.setMobilephones(String.valueOf(mobilephones));
                    }
                    faceBookBaseInfoList.add(faceBookBaseInfo);
                    //设置返回值
                    result.put("faceBookEducationInfoList", faceBookEducationInfoList);
                    result.put("faceBookWorkInfoList", faceBookWorkInfoList);
                    result.put("faceBookFamilyInfoList", faceBookFamilyInfoList);
                    result.put("faceBookPaymentsInfoList", faceBookPaymentsInfoList);
                    result.put("faceBookFriendsInfoList", faceBookFriendsInfoList);
                    result.put("faceBookPublishInfoList", faceBookPublishInfoList);
                    result.put("faceBookHistoricalLoginInfoList", faceBookHistoricalLoginInfoList);
                }
            }
            result.put(Constant.RESPONSE_DATA, faceBookBaseInfoList);
        }

        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 查询通话记录
     * @param current
     * @param pageSize
     * @param userId
     * @param phone
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getCallRecords.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> getCallRecords(@RequestParam(value = "current") Integer current,
                                                      @RequestParam(value = "pageSize") Integer pageSize,
                                                      @RequestParam(value = "userId") Long userId,String phone) throws Exception {
        Map<String, Object> result = new HashMap<>();
        if (null == userId || null == current || null == pageSize) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        UserBaseInfo userBaseInfo = userBaseInfoService.getBaseModelByUserId(userId);
        if(userBaseInfo.getUserDataId() == null){
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }else{
            Integer userDataId = userBaseInfo.getUserDataId().intValue();
            int count = 0;
            String resultJson = (String)auditDomain.getAuditInfo(userDataId, ChannelType.PSAPP, ChannelBizType.APP_CALLRECORDS).getData();
            if(resultJson != null){
                JSONArray array= JSONArray.parseArray(resultJson);
                RdPage page = new RdPage();
                List<CallRecord> realData = array.toJavaList(CallRecord.class);
                //根据phone筛选通讯记录
                if(phone != null &&realData.size() >0){
                    Iterator<CallRecord> it = realData.iterator();
                    while(it.hasNext()){
                        CallRecord model = it.next();
                        model.setStatus(0);
                        if(!model.getMatchedNumber().contains(phone)){
                            it.remove();
                        }
                    }
                }
                List<UserEmerContacts> emerContacts = userEmerContactsService.getUserEmerContactsByUserId(userId);
                if(realData.size() > 0 && emerContacts.size() > 0){
                    //判断是否匹配紧急联系人
                    for(int i = 0;i < emerContacts.size();i++){
                        for(int j = 0;j < realData.size();j++){
                            logger.info(realData.get(j).getMatchedNumber());
                            System.out.println(realData.get(j).getMatchedNumber());
                            if(realData.get(j).getMatchedNumber().contains(emerContacts.get(i).getPhone())){
                                realData.get(j).setStatus(1);
                            }
                        }
                    }
                }
                realData.sort(Comparator.comparing(CallRecord::getStatus).reversed());
                List<CallRecord> data = listToPage(realData,page,current,pageSize);
                result.put(Constant.RESPONSE_DATA, data);
                result.put("count", count);
                result.put(Constant.RESPONSE_DATA_PAGE, page);
            } else {
                RdPage page = new RdPage();
                page.setTotal(0);
                page.setPageSize(pageSize);
                page.setCurrent(current);
                page.setPages(0);
                result.put(Constant.RESPONSE_DATA, new ArrayList<>());
                result.put("count", count);
                result.put(Constant.RESPONSE_DATA_PAGE, page);
            }
            return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
        }
    }

    private  List listToPage(List data,RdPage page,Integer current,Integer pageSize){
        List realData = new ArrayList<>();
        Integer count = pageSize * current >data.size() ? data.size() : pageSize * current;
        for(int i = pageSize * (current - 1);i < count;i++){
            realData.add(data.get(i));
        }
        Integer total = data.size();
        Integer pages = data.size() / pageSize + data.size() % pageSize > 0 ? 1 : 0;
//        RdPage page = new RdPage();
        page.setCurrent(current);
        page.setPages(pages);
        page.setPageSize(pageSize);
        page.setTotal(total);
        return realData;
    }
}
