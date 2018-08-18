package com.adpanshi.cashloan.manage.controller;

import com.adpanshi.cashloan.manage.arc.model.SysUser;
import com.adpanshi.cashloan.manage.cl.enums.BorrowEnum;
import com.adpanshi.cashloan.manage.cl.model.BorrowAuditLog;
import com.adpanshi.cashloan.manage.cl.model.BorrowAuditLogWithBLOBs;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowMainModel;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowModel;
import com.adpanshi.cashloan.manage.cl.service.*;
import com.adpanshi.cashloan.manage.core.common.context.ExportConstant;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import com.adpanshi.cashloan.manage.core.common.util.excel.JsGridReportBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Scope("prototype")
@Controller
@RequestMapping("/manage/export/")
public class ManageListExport extends ManageBaseController {

    @Resource
    private BorrowService clBorrowService;
    @Resource
    private BorrowRepayLogService borrowRepayLogService;
    @Resource
    private PayLogService payLogService;
    @Resource
    private PayCheckService payCheckService;
    @Resource
    private UrgeRepayOrderService urgeRepayOrderService;
    @Resource
    private BorrowRepayService borrowRepayService;
    @Resource
    private RiskDataService riskDataService;
    @Resource
    private ChannelService channelService;
    @Resource
    private BorrowMainService borrowMainService;
    @Resource
    private BorrowAuditLogService borrowAuditLogService;
    @Resource
    private SysAttachmentService sysAttachmentService;
    @Resource
    private UserBaseInfoService userBaseInfoService;

    /**
     * 导出还款记录报表
     *
     * @throws Exception
     */
    @RequestMapping(value = "borrowRepayLog.htm")
    public void repayLogExport(
            @RequestParam(value = "searchParams", required = false) String searchParams) throws Exception {
        Map<String, Object> params = parseToMap(searchParams, true);
        List list = borrowRepayLogService.listExport(params);
        if(null == list || list.isEmpty()){
            return;
        }
        AuthUserRole user = getAuthUserRole();
        response.setContentType("application/msexcel;charset=UTF-8");
        // 记录取得
        String title = "还款记录Excel表";
        String[] hearders = ExportConstant.EXPORT_REPAYLOG_LIST_HEARDERS;
        String[] fields = ExportConstant.EXPORT_REPAYLOG_LIST_FIELDS;
        JsGridReportBase report = new JsGridReportBase(request, response);
        report.exportExcel(list, title, hearders, fields, user.getRealName());
    }

    /**
     * 导出借款订单报表
     *
     * @throws Exception
     */
    @RequestMapping(value = "borrow.htm")
    public void borrowExport(
            @RequestParam(value = "searchParams", required = false) String searchParams) throws Exception {
        Map<String, Object> params = parseToMap(searchParams, true);
        List stateList;
        if (params != null) {
            //放款列表
            String type = String.valueOf(params.get("type"));
            if ("repay".equals(type)) {
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
                String state = String.valueOf((params.get("state")));
                if (null != state && !StringUtils.isBlank(state)) {
                    params.put("state", state);
                }
            }
            String state = String.valueOf((params.get("state")));
            if (null != state && !StringUtils.isBlank(state)) {
                //还款列表
                if (state.equals(BorrowModel.STATE_FINISH)) {//40
                    stateList = Arrays.asList(BorrowModel.STATE_FINISH,
                            BorrowModel.STATE_REMISSION_FINISH);
                    params.put("stateList", stateList);
                    params.put("state", "");
                }
                //逾期中列表
                if (state.equals(BorrowModel.STATE_DELAY)) {//50
                    stateList = Arrays.asList(BorrowModel.STATE_DELAY);
                    params.put("stateList", stateList);
                    params.put("state", "");
                }
                //坏账列表
                if (state.equals(BorrowModel.STATE_BAD)) {//90
                    stateList = Arrays.asList(BorrowModel.STATE_BAD);
                    params.put("stateList", stateList);
                    params.put("state", "");
                }

            }
        }
        List list = clBorrowService.listBorrowOutModel(params);
        if(null == list || list.isEmpty()){
            return;
        }
        AuthUserRole user = getAuthUserRole();
        response.setContentType("application/msexcel;charset=UTF-8");
        // 记录取得
        String title = "借款订单Excel表";
        String[] hearders = ExportConstant.EXPORT_BORROW_LIST_HEARDERS;
        String[] fields = ExportConstant.EXPORT_BORROW_LIST_FIELDS;
        JsGridReportBase report = new JsGridReportBase(request, response);
        report.exportExcel(list, title, hearders, fields, user.getRealName());
    }

    /**
     * 导出支付记录报表
     *
     * @throws Exception
     */
    @RequestMapping(value = "payLog.htm")
    public void payLogExport(
            @RequestParam(value = "searchParams", required = false) String searchParams) throws Exception {
        Map<String, Object> searchMap=parseToMap(searchParams,true);
        List list = payLogService.listPayLog(searchMap);
        if(null == list || list.isEmpty()){
            return;
        }
        AuthUserRole user = getAuthUserRole();
        response.setContentType("application/msexcel;charset=UTF-8");
        // 记录取得
        String title = "支付记录Excel表";
        String[] hearders = ExportConstant.EXPORT_PAYLOG_LIST_HEARDERS;
        String[] fields = ExportConstant.EXPORT_PAYLOG_LIST_FIELDS;
        JsGridReportBase report = new JsGridReportBase(request, response);
        report.exportExcel(list, title, hearders, fields, user.getRealName());
    }



    /**
     * 导出催收订单报表
     *
     * @throws Exception
     */
    @RequestMapping(value = "urgeRepayOrder.htm")
    public void urgeRepayOrderExport(
            @RequestParam(value = "searchParams", required = false) String searchParams) throws Exception {
        Map<String, Object> params = parseToMap(searchParams, true);
        List list = urgeRepayOrderService.listUrgeRepayOrder(params);
        if(null == list || list.isEmpty()){
            return;
        }
        AuthUserRole user = getAuthUserRole();
        response.setContentType("application/msexcel;charset=UTF-8");
        // 记录取得
        String title = "催收订单Excel表";
        String[] hearders = ExportConstant.EXPORT_REPAYORDER_LIST_HEARDERS;
        String[] fields = ExportConstant.EXPORT_REPAYORDER_LIST_FIELDS;
        JsGridReportBase report = new JsGridReportBase(request, response);
        report.exportExcel(list, title, hearders, fields, user.getRealName());
    }

    /**
     * 导出催收反馈报表
     *
     * @throws Exception
     */
    @RequestMapping(value = "urgeLog.htm")
    public void urgeLogExport(
            @RequestParam(value = "searchParams", required = false) String searchParams) throws Exception {
        Map<String, Object> params = parseToMap(searchParams, true);
        List list = urgeRepayOrderService.listUrgeLog(params);
        if(null == list || list.isEmpty()){
            return;
        }
        AuthUserRole user = getAuthUserRole();
        response.setContentType("application/msexcel;charset=UTF-8");
        // 记录取得
        String title = "催收反馈Excel表";
        String[] hearders = ExportConstant.EXPORT_URGELOG_LIST_HEARDERS;
        String[] fields = ExportConstant.EXPORT_URGELOG_LIST_FIELDS;
        JsGridReportBase report = new JsGridReportBase(request, response);
        report.exportExcel(list, title, hearders, fields, user.getRealName());
    }


    /**
     * 导出日报数据统计报表
     *
     * @throws Exception
     */
    @RequestMapping(value = "dailyDataLog.htm")
    public void dailyDataExport(
            @RequestParam(value = "searchParams", required = false) String searchParams) throws Exception {
        Map<String, Object> params = parseToMap(searchParams, true);
        List list = riskDataService.listdailyData(params);
        if(null == list || list.isEmpty()){
            return;
        }
        AuthUserRole user = getAuthUserRole();
        response.setContentType("application/msexcel;charset=UTF-8");
        // 记录取得
        String title = "日报统计Excel表";
        String[] hearders = ExportConstant.EXPORT_DAILYDATA_LIST_HEARDERS;
        String[] fields = ExportConstant.EXPORT_DAILYDATA_LIST_FIELDS;
        JsGridReportBase report = new JsGridReportBase(request, response);
        report.exportExcel(list, title, hearders, fields, user.getRealName());
    }

    /**
     * 导出还款计划报表
     *
     * @throws Exception
     */
    @RequestMapping(value = "Repaymentplan.htm", method = {RequestMethod.GET})
    public void repaymentPlangExport(
            @RequestParam(value = "searchParams", required = false) String searchParams) throws Exception {
        Map<String, Object> params = parseToMap(searchParams, true);
        if (null != params.get("auditName")) {
            params.put("auditName", URLDecoder.decode((String) params.get("auditName"), "utf-8"));
        }
        if (null != params.get("realName")) {
            params.put("realName", URLDecoder.decode((String) params.get("realName"), "utf-8"));
        }
        List list = borrowRepayService.listExport(params);
        if(null == list || list.isEmpty()){
            return;
        }
        AuthUserRole user = getAuthUserRole();
        response.setContentType("application/msexcel;charset=UTF-8");
        // 记录取得
        String title = "还款计划Excel表";
        String[] hearders = ExportConstant.EXPORT_BORROWREPAY_LIST_HEARDERS;
        String[] fields = ExportConstant.EXPORT_BORROWREPAY_LIST_FIELDS;
        JsGridReportBase report = new JsGridReportBase(request, response);
        report.exportExcel(list, title, hearders, fields, user.getRealName());
    }





    /**
     * 批量推送文件模版下载
     */
    @RequestMapping(value = "appMsgModelFileDownload.htm", method = {RequestMethod.GET})
    public void modelFileDownload() throws Exception {
        response.setContentType("application/msexcel;charset=UTF-8");
        // 记录取得
        String title = "批量推送文件模版表";
        String[] hearders = new String[]{"推送手机号（仅需复制到此列）"};
        JsGridReportBase report = new JsGridReportBase(request, response);
        report.exportModelExcel(title, hearders);
    }

    /**
     * 导出订单信息报表
     */
    @RequestMapping(value = "orderRefuse.htm", method = {RequestMethod.GET})
    public void orderRefuseExport(
            @RequestParam(value = "searchParams", required = false) String searchParams) throws Exception {
        Map<String, Object> params = parseToMap(searchParams, true);
        List borrowMainPage = null;
        if (params != null) {
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
            //中文转码
            if (null != params.get("realName")) {
                params.put("realName", URLDecoder.decode((String) params.get("realName"), "utf-8"));
            }
            params.remove("state");
            params.put("stateList",stateList);
            borrowMainPage = borrowMainService.selectBorrowList(params, 0, 0);
            //审核记录
            for (Object page : borrowMainPage){
                BorrowAuditLogWithBLOBs data = borrowAuditLogService.findLogs(((BorrowMainModel)page).getId());
                if(null != data){
                    ((BorrowMainModel)page).setAuditData(data.getAuditData());
                }
            }
        } else {
            throw new Exception("必须带参数进行查询!");
        }
        if(null == borrowMainPage || borrowMainPage.isEmpty()){
            return;
        }
        AuthUserRole user = getAuthUserRole();
        response.setContentType("application/msexcel;charset=UTF-8");
        // 记录取得
        String title = "订单审批信息表";
        String[] hearders = ExportConstant.EXPORT_SHENPI_LIST_HEADERS;
        String[] fields = ExportConstant.EXPORT_SHENPI_LIST_FIELDS;
        JsGridReportBase report = new JsGridReportBase(request, response);
        report.exportExcel(borrowMainPage, title, hearders, fields, user.getRealName());
    }


    /**
     * 批量推送文件模版下载
     */
    @RequestMapping(value = "getImg.htm", method = {RequestMethod.GET})
    public void getImg() throws Exception {
        sysAttachmentService.getImg(request,response);
    }
    /**
     * 导出放款订单用户信息
     */
    @RequestMapping(value = "loanExport.htm", method = {RequestMethod.GET})
    public void loanExport() throws Exception {
        List list = userBaseInfoService.findLoanUserInfo();
        if(list.size() == 0){
            return;
        }
        response.setContentType("application/msexcel;charset=UTF-8");
        // 记录取得
        String title = "放款信息统计报表";
        String[] hearders = ExportConstant.EXPORT_LOAN_USER_HEADERS;
        String[] fields = ExportConstant.EXPORT_LOAN_USER_FIELDS;
        JsGridReportBase report = new JsGridReportBase(request, response);
        report.exportExcel(list, title, hearders, fields, getAuthUserRole().getRealName());
    }
}
