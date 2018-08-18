package com.adpanshi.cashloan.manage.controller;

import com.adpanshi.cashloan.manage.cl.service.SystemCountService;
import com.adpanshi.cashloan.manage.pojo.ResultModel;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 首页工作台controller
 * @date 2018/8/1 19:56
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/index/")
public class ManageIndexController extends ManageBaseController{
    @Resource
    private SystemCountService systemCountService;

    /**
     * @auther lifei
     * @description 首页今日数据统计
     * @data 2017-12-16
     */
    @RequestMapping(value = "homeInfoTodayCount.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> homeInfoTodayCount() throws Exception {
        //先获取缓存
        Map<String,Object> todayCount=manager.getHomeInfoRedisCashe(Thread.currentThread() .getStackTrace()[1].getMethodName().toUpperCase());
        //没有则查询
        if(null == todayCount){
            todayCount=systemCountService.systemTodayCount();
            //存入
            manager.createHomeInfoRedisCashe(Thread.currentThread() .getStackTrace()[1].getMethodName().toUpperCase(),todayCount);
        }
        return new ResponseEntity<>(ResultModel.ok(todayCount), HttpStatus.OK);
    }

    /**
     * @auther lifei
     * @description 首页累计数据统计
     * @data 2017-12-16
     */
    @RequestMapping(value = "homeInfoCumulativeCount.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> homeInfoCumulativeConut() throws Exception {
        //先获取缓存
        Map<String,Object> cumulativeCount=manager.getHomeInfoRedisCashe(Thread.currentThread() .getStackTrace()[1].getMethodName().toUpperCase());
        //没有则查询
        if(null == cumulativeCount){
            cumulativeCount=systemCountService.systemCumulativeCount();
            //存入
            manager.createHomeInfoRedisCashe(Thread.currentThread() .getStackTrace()[1].getMethodName().toUpperCase(),cumulativeCount);
        }
        return new ResponseEntity<>(ResultModel.ok(cumulativeCount), HttpStatus.OK);
    }

    /**
     * @auther lifei
     * @description 首页实时数据统计
     * @data 2017-12-16
     */
    @RequestMapping(value = "homeInfoRealTimeCount.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> homeInfoRealTimeCount() throws Exception {
        //先获取缓存
        Map<String,Object> realTimeCount=manager.getHomeInfoRedisCashe(Thread.currentThread() .getStackTrace()[1].getMethodName().toUpperCase());
        //没有则查询
        if(null == realTimeCount){
            realTimeCount=systemCountService.systemRealTimeCount();
            //存入
            manager.createHomeInfoRedisCashe(Thread.currentThread() .getStackTrace()[1].getMethodName().toUpperCase(),realTimeCount);
        }
        return new ResponseEntity<>(ResultModel.ok(realTimeCount), HttpStatus.OK);
    }

    /**
     * @auther lifei
     * @description 首页地域数据统计
     * @data 2017-12-16
     */
    @RequestMapping(value = "homeInfoCountByProvince.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> homeInfoCountByProvince() throws Exception {
        //先获取缓存
        Map<String,Object> countByProvince=manager.getHomeInfoRedisCashe(Thread.currentThread() .getStackTrace()[1].getMethodName().toUpperCase());
        //没有则查询
        if(null == countByProvince){
            countByProvince=systemCountService.systemCountByProvince();
            //存入
            manager.createHomeInfoRedisCashe(Thread.currentThread() .getStackTrace()[1].getMethodName().toUpperCase(),countByProvince);
        }
        return new ResponseEntity<>(ResultModel.ok(countByProvince), HttpStatus.OK);
    }

    /**
     * @auther lifei
     * @description 首页还款方式统计
     * @data 2017-12-16
     */
    @RequestMapping(value = "homeInfoCountRepaySource.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> homeInfoCountRepaySource() throws Exception {
        //先获取缓存
        Map<String,Object> repaySourceCount=manager.getHomeInfoRedisCashe(Thread.currentThread() .getStackTrace()[1].getMethodName().toUpperCase());
        //没有则查询
        if(null == repaySourceCount){
            repaySourceCount=systemCountService.systemRepaySourceCount();
            //存入
            manager.createHomeInfoRedisCashe(Thread.currentThread() .getStackTrace()[1].getMethodName().toUpperCase(),repaySourceCount);
        }
        return new ResponseEntity<>(ResultModel.ok(repaySourceCount), HttpStatus.OK);
    }

    /**
     * @auther lifei
     * @description 首页每天放款量，应还款量与实还款量统计
     * @data 2017-1-1*
     */
    @RequestMapping(value = "homeInfoFifteenDaysCount.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> homeInfoFifteenDaysCount() throws Exception {
        //先获取缓存
        Map<String,Object> fifteenDaysSourceCount=manager.getHomeInfoRedisCashe(Thread.currentThread() .getStackTrace()[1].getMethodName().toUpperCase());
        //没有则查询
        if(null == fifteenDaysSourceCount){
            fifteenDaysSourceCount=systemCountService.systemFifteenDaysSourceCount();
            //存入
            manager.createHomeInfoRedisCashe(Thread.currentThread() .getStackTrace()[1].getMethodName().toUpperCase(),fifteenDaysSourceCount);
        }
        return new ResponseEntity<>(ResultModel.ok(fifteenDaysSourceCount), HttpStatus.OK);
    }
}
