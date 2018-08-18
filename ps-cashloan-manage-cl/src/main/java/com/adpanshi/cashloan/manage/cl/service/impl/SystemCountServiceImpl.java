package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.expand.SystemCountMapper;
import com.adpanshi.cashloan.manage.cl.service.SystemCountService;
import com.adpanshi.cashloan.manage.core.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import tool.util.BigDecimalUtil;
import tool.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Vic Tang
 * @Description: 首页统计serviceImpl
 * @date 2018/8/1 20:03
 */
@Service("systemCountService")
public class SystemCountServiceImpl implements SystemCountService {
    private static final Logger logger = LoggerFactory.getLogger(SystemCountServiceImpl.class);
    @Resource
    private SystemCountMapper systemCountMapper;
    @Override
    public Map<String, Object> systemTodayCount() throws Exception {
        Map<String,Object> rtMap = new HashMap<String, Object>();
        //  统计当日注册用户数量
        Integer register = systemCountMapper.countRegister();
        rtMap.put("register", register);
        //统计当日登陆用户数量
        Integer login = systemCountMapper.countLogin();
        rtMap.put("login", login);
        //统计当日借款申请的数量
        double borrow = systemCountMapper.countBorrow();
        rtMap.put("borrow", borrow);
        //统计当日借款申请通过的数量
        double borrowPass = systemCountMapper.countBorrowPass();
        rtMap.put("borrowPass", borrowPass);
        //通过率
        if(borrow>0){
            rtMap.put("passApr", BigDecimalUtil.decimal(borrowPass/borrow*100L,2));
        }else{
            rtMap.put("passApr", 0);
        }
        //统计当日借款申请放款数量
        Integer borrowLoan = systemCountMapper.countBorrowLoan();
        rtMap.put("borrowLoan", borrowLoan);
        //统计当日还款量
        Integer borrowRepay = systemCountMapper.countBorrowRepay();
        rtMap.put("borrowRepay", borrowRepay);
        return rtMap;
    }

    @Override
    public Map<String, Object> systemRealTimeCount() throws Exception {
        Map<String,Object> rtMap = new HashMap<String, Object>();
        //待还款总额
        Double needRepay  = systemCountMapper.sumBorrowNeedRepay();
        rtMap.put("needRepay", needRepay==null?0.00:needRepay);
        //逾期未还款总额
        Double overdueRepay = systemCountMapper.sumBorrowOverdueRepay();
        rtMap.put("overdueRepay", overdueRepay==null?0.00:overdueRepay);
        //逾期未还款罚金总额
        Double fineRepay = systemCountMapper.sumBorrowOverdueFineRepay();
        rtMap.put("fineRepay", fineRepay==null?0.00:fineRepay);
        return rtMap;
    }

    @Override
    public Map<String, Object> systemCumulativeCount() throws Exception {
        Map<String,Object> rtMap = new HashMap<String, Object>();
        //统计历史放款总量
        Integer borrowMainLoanHistory = systemCountMapper.countBorrowMainLoanHistory();
        rtMap.put("borrowMainLoanHistory", borrowMainLoanHistory);
        //统计历史还款总量
        Integer borrowMainRepayHistory = systemCountMapper.countBorrowMainRepayHistory();
        rtMap.put("borrowMainRepayHistory", borrowMainRepayHistory);
        //历史回款率
        Double MainRepaymentRate = systemCountMapper.MainRepaymentRate();
        rtMap.put("MainRepaymentRate", MainRepaymentRate);
        //统计分期放款总量
        Integer BorrowLoanHistory = systemCountMapper.countBorrowLoanHistory();
        rtMap.put("BorrowLoanHistory", BorrowLoanHistory);
        //统计分期还款总量
        Integer borrowRepayHistory = systemCountMapper.countBorrowRepayHistory();
        rtMap.put("borrowRepayHistory", borrowRepayHistory);
        //分期回款率
        Double RepaymentRate = systemCountMapper.RepaymentRate();
        rtMap.put("RepaymentRate", RepaymentRate);
        return rtMap;
    }

    @Override
    public Map<String, Object> systemCountByProvince() throws Exception {
        Map<String,Object> rtMap = new HashMap<String, Object>();
        Map<String,Object> result = null;
        List<Map<String,Object>> rtValue = null;
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext context = webApplicationContext.getServletContext();
        if (StringUtil.isNotBlank(context)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Object t  = context.getAttribute("monthCountSelectTime");
            int now = DateUtil.getDay(DateUtil.getNow());
            if (t==null||DateUtil.getDay(sdf.parse(t.toString()))!=now) {
                //当月融资金额(按地区分组)
                rtValue = systemCountMapper.sumMonthBorrowAmtByProvince();
                result = reBuildMap(rtValue);
                rtMap.put("monthBorrowAmt", result);
                context.setAttribute("monthBorrowAmt", rtValue);
                //当月借款次数(按地区分组)
                rtValue = systemCountMapper.countMonthBorrowByProvince();
                result = reBuildMap(rtValue);
                rtMap.put("monthBorrowCount", result);
                context.setAttribute("monthBorrowCount", rtValue);
                //当月还款金额(按地区分组)
                rtValue = systemCountMapper.sumMonthBorrowRepayByProvince();
                result = reBuildMap(rtValue);
                rtMap.put("monthBorrowRepay", result);
                context.setAttribute("monthBorrowRepay", rtValue);
                //当月新增用户(按地区分组)
                rtValue = systemCountMapper.countMonthRegisterByProvince();
                result = reBuildMap(rtValue);
                rtMap.put("monthRegister", result);
                context.setAttribute("monthRegister", rtValue);
                context.setAttribute("monthCountSelectTime", sdf.format(now));//保存时间
            }else {
                rtValue = (List<Map<String, Object>>) context.getAttribute("monthBorrowAmt");
                result = reBuildMap(rtValue);
                rtMap.put("monthBorrowAmt", result);
                rtValue = (List<Map<String, Object>>) context.getAttribute("monthBorrowCount");
                result = reBuildMap(rtValue);
                rtMap.put("monthBorrowCount", result);
                rtValue = (List<Map<String, Object>>) context.getAttribute("monthBorrowRepay");
                result = reBuildMap(rtValue);
                rtMap.put("monthBorrowRepay", result);
                rtValue = (List<Map<String, Object>>) context.getAttribute("monthRegister");
                result = reBuildMap(rtValue);
                rtMap.put("monthRegister", result);
            }
        }
        return rtMap;
    }

    @Override
    public Map<String, Object> systemRepaySourceCount() throws Exception {
        Map<String,Object> rtMap = new HashMap<String, Object>();
        Map<String,Object> result = null;
        List<Map<String,Object>> rtValue = null;
        rtValue = systemCountMapper.countRepaySource();
        result = reBuildMap(rtValue);
        String[] source = {"自动代扣","银行卡转账","支付宝转账","主动还款","其它"};
        List<Map<String,Object>> sourceList = new ArrayList<Map<String,Object>>();
        Map<String,Object> sm;
        for(int i=0;i<source.length;i++){
            if(!result.containsKey(source[i])){
                result.put(source[i], 0);
            }
            sm = new HashMap<String, Object>();
            sm.put(source[i], result.get(source[i]));
            sourceList.add(sm);
        }
        rtMap.put("repaySource", sourceList);
        return rtMap;
    }

    @Override
    public Map<String, Object> systemFifteenDaysSourceCount() throws Exception {
        Map<String,Object> rtMap = new HashMap<String, Object>();
        List<String> days = new ArrayList<String>();
        Date nowDate = DateUtil.getNow();
        days.add(DateUtil.dateStr(nowDate, DateUtil.DATEFORMAT_STR_002));
        Calendar date = Calendar.getInstance();
        for(int i=0;i<15;i++){
            date.setTime(nowDate);
            date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
            nowDate = date.getTime();
            String day = DateUtil.dateStr(nowDate, DateUtil.DATEFORMAT_STR_002);
            days.add(day);
        }
        systemCountMapper.countFifteenDaysNeedRepay();
        //最近15日应还款量
        List<Map<String,Object>> rtValue1 = systemCountMapper.countFifteenDaysNeedRepay();
        //最近15日实际还款量
        List<Map<String,Object>> rtValue2 = systemCountMapper.countFifteenDaysRealRepay();
        //最近15日每天放款量
        List<Map<String,Object>> rtValue4 = systemCountMapper.countFifteenDaysLoan();
        Map<String,Object> result1 = reBuildMap(rtValue1);
        Map<String,Object> result2 = reBuildMap(rtValue2);
        Map<String,Object> result4 = reBuildMap(rtValue4);
        Map<String,Object> result3 = new HashMap<String, Object>();
        for(int i=0;i<days.size();i++){
            String day = days.get(i);
            if(!result1.containsKey(day)){
                result1.put(day, 0.00);
            }
            if(!result2.containsKey(day)){
                result2.put(day, 0.00);
            }
            String needStr = String.valueOf(result1.get(day));
            needStr = (StringUtil.isNotBlank(needStr) && !"null".equals(needStr))?needStr:"0.00";
            String realStr = String.valueOf(result2.get(day));
            realStr = (StringUtil.isNotBlank(realStr) && !"null".equals(realStr))?realStr:"0.00";
            Double need = Double.valueOf(needStr);
            Double real = Double.valueOf(realStr);
            if(real>=need){
                result3.put(day, 0.00);
            }else if(real<need){
                Double diff = need - real;
                result3.put(day, diff/need);
            }else{
                result3.put(day, 1.0);
            }
            if(!result4.containsKey(day)){
                result4.put(day, 0);
            }
        }
        rtMap.put("fifteenDaysNeedRepay", result1);
        rtMap.put("fifteenDaysRealRepay", result2);
        rtMap.put("fifteenDaysOverdueApr", result3);
        rtMap.put("fifteenDaysLoan", result4);
        return rtMap;
    }

    public Map<String,Object> reBuildMap(List<Map<String,Object>> maps){
        if(maps!=null){
            Map<String,Object> result = new HashMap<String, Object>();
            for(int i=0;i<maps.size();i++){
                String key = String.valueOf(maps.get(i).get("key"));
                if(StringUtil.isNotBlank(key)){
                    key = key==null?"":key;
                }else{
                    key = "未知地区";
                }
                Object value = maps.get(i).get("value");
                result.put(key, value);
            }
            result.remove("null");
            return result;
        }else{
            return new HashMap<String, Object>();
        }
    }
}
