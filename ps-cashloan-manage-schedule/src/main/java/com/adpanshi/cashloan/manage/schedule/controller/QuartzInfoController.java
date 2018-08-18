package com.adpanshi.cashloan.manage.schedule.controller;



import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.manage.cl.enums.QuartzEnum;
import com.adpanshi.cashloan.manage.cl.model.QuartzInfo;
import com.adpanshi.cashloan.manage.cl.model.expand.QuartzInfoModel;
import com.adpanshi.cashloan.manage.cl.model.expand.QuartzLogModel;
import com.adpanshi.cashloan.manage.cl.service.QuartzInfoService;
import com.adpanshi.cashloan.manage.cl.service.QuartzLogService;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.util.RdPage;
import com.adpanshi.cashloan.manage.schedule.pojo.QuartzManager;
import com.adpanshi.cashloan.manage.schedule.pojo.ResultModel;
import com.github.pagehelper.Page;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tool.util.StringUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 定时任务Controller
 *
 * @author huangqin
 * @version 1.1.0
 * @date 2018-01-01 15:02
 */
@Scope("prototype")
@Controller
@RequestMapping("/schedule/quartz/")
public class QuartzInfoController extends ManageBaseController {

    @Resource
    private QuartzInfoService quartzInfoService;

    @Resource
    private QuartzLogService quartzLogService;

    /****** 任务列表 ******/
    /**
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 定时任务-任务列表-下拉列表查询
     * @data 2018-01-01 20:58
     */
    @RequestMapping(value = "quartzDropDownList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> quartzDropDownList() {
        this.logger.info("------定时任务-任务列表-下拉列表查询------");
        //读库标志
        Map<String, Object> result = new HashMap<>();
        result.put("quartzList", quartzInfoService.list(new HashMap<String, Object>()));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 定时任务列表-查询
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     * @throws Exception
     * @author huangqin
     * @date 2018-01-01 15:04
     */
    @RequestMapping(value = "list.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> list(@RequestParam(value = "searchParams") String searchParams,
                                            @RequestParam(value = "current") Integer current,
                                            @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        logger.info("------定时任务-任务列表-查询------");
        Map<String, Object> searchMap = parseToMap(searchParams, true);
        Page<QuartzInfoModel> page = quartzInfoService.page(searchMap, current, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 启动/禁用定时任务
     *
     * @param id
     * @throws Exception
     * @author huangqin
     * @date 2018-01-01 15:08
     */
    @RequestMapping(value = "enableOrDisable.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> execute(@RequestParam(value = "id") Long id) {
        logger.info("------定时任务-任务列表-启动/禁用任务------【启动/禁用任务】开始...");
        QuartzInfo quartzInfo = quartzInfoService.getById(id);
        if (null == quartzInfo || StringUtil.isBlank(quartzInfo.getClassName())) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.QUART_NO_QUART_CODE_VALUE), HttpStatus.OK);
        }
        try {
            Map<String, Object> data = new HashMap<>();
            QuartzInfo info = new QuartzInfo();
            info.setId(quartzInfo.getId());
            if (QuartzInfoModel.STATE_DISABLE.equals(quartzInfo.getState())) {
                // 任务添加
                QuartzManager.addJob(quartzInfo.getCode(), Class.forName(quartzInfo.getClassName()).newInstance().getClass(), quartzInfo.getCycle());
                info.setState(QuartzInfoModel.STATE_ENABLE);
            } else if (QuartzInfoModel.STATE_ENABLE.equals(quartzInfo.getState())) {
                //任务移除
                QuartzManager.removeJob(quartzInfo.getCode());
                info.setState( QuartzInfoModel.STATE_DISABLE);
            } else {
                //状态异常
                return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.QUART_NO_QUART_STATE_CODE_VALUE), HttpStatus.OK);
            }
            if (!quartzInfoService.update(info)) {
                return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_INSERT_CODE_VALUE), HttpStatus.OK);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            logger.info("【启动/禁用任务】异常...", e);
        }
        logger.info("【启动/禁用任务】结束...");
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * 定时任务-立即执行一个任务
     *
     * @param id
     * @return ResponseEntity<ResultModel>
     * @throws Exception
     * @author huangqin
     * @date 2018-01-01 15:10
     */
    @RequestMapping(value = "runJobNow.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> runJobNow(@RequestParam(value = "id") Long id) throws Exception {
        logger.info("------定时任务-任务列表-查询------【执行任务】开始...");
        QuartzInfo quartzInfo = quartzInfoService.getById(id);
        if (null == quartzInfo || QuartzInfoModel.STATE_DISABLE.equals(quartzInfo.getState())) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.QUART_NO_QUART_OR_DISABLED_STATE_CODE_VALUE), HttpStatus.OK);
        }
        QuartzManager.startJobNow(quartzInfo.getCode());
        logger.info("【执行任务】结束...");
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }


    /**
     * 添加/修改定时任务
     *
     * @param quartzInfo
     * @return ResponseEntity<ResultModel>
     * @throws Exception
     * @author huangqin
     * @date 2018-01-01 15:15
     */
    @RequestMapping(value = "addOrUpdtae.htm", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<ResultModel> addOrUpdtae(QuartzInfo quartzInfo) throws Exception {
        logger.info("------定时任务-任务列表-添加/修改定时任务------");
        if (null == quartzInfo) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        Map<String, Object> paramMap = new HashMap<>();
        if (null == quartzInfo.getId() || quartzInfo.getId().compareTo(0L) <= 0) {
            //添加
            if (null != quartzInfoService.findSelective(quartzInfo.getCode())) {
                return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.QUART_QUART_EXIST_CODE_VALUE), HttpStatus.OK);
            }
            if (!quartzInfoService.save(quartzInfo)) {
                return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_INSERT_CODE_VALUE), HttpStatus.OK);
            }
        } else {
            //修改
            QuartzInfo quartzInfoExist = quartzInfoService.getById(quartzInfo.getId());
            //修改任务
            if (null != quartzInfoExist && QuartzInfoModel.STATE_ENABLE.equals(quartzInfoExist.getState())) {
                QuartzManager.modifyJobTime(quartzInfoExist.getCode(), quartzInfo.getCycle());
            }
            //更新数据库
            QuartzInfo info = new QuartzInfo();
            info.setId(quartzInfo.getId());
            info.setName(quartzInfo.getName());
            info.setCycle(quartzInfo.getCycle());
            if (!quartzInfoService.update(info)) {
                return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_UPDATE_CODE_VALUE), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }


    /****** 执行记录 ******/

    /**
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 定时任务-执行记录-下拉列表查询
     * @data 2018-01-01 21:02
     */
    @RequestMapping(value = "quartzLogDropDownList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> quartzLogDropDownList() {
        this.logger.info("------定时任务-执行记录-下拉列表查询------");
        //读库标志
        Map<String, Object> result = new HashMap<>();
        result.put("quartzList", quartzInfoService.list(new HashMap<String, Object>()));
        result.put("resultList", QuartzEnum.EXCUTE_RESULT.EnumValueS());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * 定时日志列表
     *
     * @param searchParams
     * @param current
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     * @throws Exception
     * @author huangqin
     * @date 2018-01-01 15:15
     */
    @RequestMapping(value = "logPage.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> quartzLog(@RequestParam(value = "searchParams") String searchParams,
                                                 @RequestParam(value = "current") Integer current,
                                                 @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        logger.info("------定时任务-执行记录-查询------");
        Map<String, Object> searchMap = parseToMap(searchParams, true);
        Page<QuartzLogModel> page = quartzLogService.page(searchMap, current, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
}