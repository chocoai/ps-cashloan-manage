package com.adpanshi.cashloan.manage.controller;


import com.adpanshi.cashloan.manage.cl.service.OverdueStatisticService;
import com.adpanshi.cashloan.manage.cl.service.RegisterStatisticService;
import com.adpanshi.cashloan.manage.pojo.ResultModel;
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
 * @Description: 注册统计Controller
 * @date 2018/8/20 11:01
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/statisticReport/")
public class ManageStatisticReportController extends ManageBaseController{
    @Resource
    private RegisterStatisticService registerStatisticService;
    @Resource
    private OverdueStatisticService overdueStatisticService;
    /**
     *  查看用户注册信息分布情况
     * @param time 统计时间
     * @throws
     * @author Vic Tang
     * @date 2018/8/22 17:44
     * */
    @RequestMapping(value = "getStatisticByDistribution.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> getStatisticByDistribution(@RequestParam(value = "time") String time) throws Exception {
        Map<String,Object> registerStatistic = registerStatisticService.getStatisticByDistribution(time);
        return new ResponseEntity<>(ResultModel.ok(registerStatistic), HttpStatus.OK);
    }
    /**
     *  查看用户认证统计情况
     * @param time 统计时间
     * @param range 统计范围
     * @throws
     * @author Vic Tang
     * @date 2018/8/22 17:44
     * */
    @RequestMapping(value = "getStatisticByAuth.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> getStatisticByAuth(@RequestParam(value = "time") String time, String range){
        Map<String,Object> result = registerStatisticService.getStatisticByAuth(time,range);
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }
    /**
     *  查询当前时间的统计数据
     * @param time
     * @throws
     * @author Vic Tang
     * @date 2018/8/27 14:42
     * */
    @RequestMapping(value = "getStatisticIndex.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> getStatisticIndex(@RequestParam(value = "time") String time){
        Map<String,Object> result = new HashMap<>();
        result.put("register",registerStatisticService.getStatisticIndex(time));
        result.put("overdue",overdueStatisticService.getStatisticIndex());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }


}
