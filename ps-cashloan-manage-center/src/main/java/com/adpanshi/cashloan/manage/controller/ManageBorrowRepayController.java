package com.adpanshi.cashloan.manage.controller;

import com.adpanshi.cashloan.manage.arc.model.SysUser;
import com.adpanshi.cashloan.manage.cl.model.BorrowRepay;
import com.adpanshi.cashloan.manage.cl.pojo.RepayExcelModel;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowRepayModel;
import com.adpanshi.cashloan.manage.cl.service.BorrowRepayService;
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
import org.springframework.web.multipart.MultipartFile;
import tool.util.DateUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author Vic Tang
 * @Description: 还款计划controller
 * @date 2018/7/31 17:45
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/borrowRepay/")
public class ManageBorrowRepayController extends ManageBaseController{
    @Resource
    private BorrowRepayService borrowRepayService;

    /**
     * 还款计划查询所有下拉条件
     *
     * @return ResponseEntity<ResultModel>
     * @author: huangqin
     * @date : 2018-01-04
     */
    @RequestMapping(value = "repayListDropDownList.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> repayListDropDownList() {
        logger.info("------还款管理-还款计划下拉查询------");
        Map<String, Object> result = new HashMap<>();
        //还款状态
        result.put("repayState", BorrowRepayEnum.REPAY_STATE.EnumValueS());
        //还款方式
        result.put("repayWay", BorrowRepayEnum.REPAY_WAY.EnumValueS());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 还款计划列表
     *
     * @param searchParams
     * @param currentPage
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     * @author: huangqin
     * @date : 2018-01-04
     */
    @RequestMapping(value = "list.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> repayList(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                 @RequestParam(value = "current") Integer currentPage,
                                                 @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        logger.info("------还款管理-还款计划查询------");
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<BorrowRepayModel> page = borrowRepayService.listModel(params, currentPage, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 确认还款
     *
     * @param id           还款计划id
     * @param amount       还款金额
     * @param penaltyAmout 逾期罚息
     * @param repayTime    还款时间
     * @param repayWay     还款方式
     * @param serialNumber 流水号
     * @param repayAccount 还款账号
     * @param state        正常还款  10  ，金额减免 20
     * @author: huangqin
     * @date : 2018-01-04
     */
    @RequestMapping(value = "confirmRepay.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> confirmRepay(@RequestParam(value = "id") Long id,
                                                    @RequestParam(value = "amount", required = false) Double amount,
                                                    @RequestParam(value = "penaltyAmout", required = false) Double penaltyAmout,
                                                    @RequestParam(value = "repayTime") String repayTime,
                                                    @RequestParam(value = "repayWay") String repayWay,
                                                    @RequestParam(value = "serialNumber") String serialNumber,
                                                    @RequestParam(value = "repayAccount") String repayAccount,
                                                    @RequestParam(value = "derateAmount", required = false) Double derateAmount,
                                                    @RequestParam(value = "state") String state) throws Exception {
        logger.info("------还款管理-还款计划-确认还款------");
        Map<String, Object> param = new HashMap<>();
        SysUser user =getLoginUser();
        param.put("id", id);
        param.put("repayTime", DateUtil.valueOf(repayTime, DateUtil.DATEFORMAT_STR_001));
        param.put("repayWay", repayWay);
        param.put("repayAccount", repayAccount);
        param.put("amount", amount);
        param.put("serialNumber", serialNumber);
        param.put("penaltyAmout", penaltyAmout);
        param.put("state", state);
        param.put("derateAmount", derateAmount);
        param.put("confirmTime",new Date());
        param.put("confirmId",user.getId());
        BorrowRepay br = borrowRepayService.getById(id);
        if (null == br) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MANAGE_REPAY_NO_REPAYBORROW_CODE_VALUE), HttpStatus.OK);
        }
        if (BorrowRepayModel.STATE_REPAY_YES.equals(br.getState())) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MANAGE_REPAY_BORROW_REPAYED_CODE_VALUE), HttpStatus.OK);
        }
        borrowRepayService.confirmRepayNew(param);

        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * 文件上传批量还款
     *
     * @param repayFile
     * @param type
     * @return ResponseEntity<ResultModel>
     * @author: huangqin
     * @date : 2018-01-04
     */
    @RequestMapping(value = "fileBatchRepay.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> fileBatchRepay(@RequestParam(value = "repayFile") MultipartFile repayFile,
                                                      @RequestParam(value = "type") String type) throws Exception {
        logger.info("------还款管理-还款信息-批量还款------");
        Map<String, Object> result = new HashMap<>();
        Long userId = getLoginUser().getId();
        List<List<String>> list = borrowRepayService.fileBatchRepay(repayFile, type,userId);
        String title = "批量还款匹配结果";
        RepayExcelModel report = new RepayExcelModel();
        String fileName = report.saveExcelByList(list, title, repayFile.getOriginalFilename(), request);
        result.put(Constant.RESPONSE_DATA,  fileName);
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
    /**
     * 指定文件下载
     */
    @RequestMapping(value = "downRepayByFile.htm", method = {RequestMethod.GET})
    public void fileBatchRepay(@RequestParam(value = "path") String path) throws IOException {
        new RepayExcelModel().exportFile(URLDecoder.decode(path,"UTF-8"), request, response);
    }
}
