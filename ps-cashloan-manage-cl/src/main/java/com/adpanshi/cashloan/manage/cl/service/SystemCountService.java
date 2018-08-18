package com.adpanshi.cashloan.manage.cl.service;

import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 首页数据统计service
 * @date 2018/8/1 19:59
 */
public interface SystemCountService {
    /**
     *  今日统计
     * @param
     * @return Map<String,Object>
     * @throws
     * @author Vic Tang
     * @date 2018/8/1 20:00
     * */
    Map<String,Object> systemTodayCount()throws Exception;
    /**
     *  实时统计
     * @param
     * @return Map<String,Object>
     * @throws
     * @author Vic Tang
     * @date 2018/8/1 20:00
     * */
    Map<String, Object> systemRealTimeCount() throws Exception;
    /**
     *  累计统计
     * @param
     * @return Map<String,Object>
     * @throws
     * @author Vic Tang
     * @date 2018/8/1 20:00
     * */
    Map<String, Object> systemCumulativeCount() throws Exception;
    /**
     *  地域统计
     * @param
     * @return Map<String,Object>
     * @throws
     * @author Vic Tang
     * @date 2018/8/1 20:00
     * */
    Map<String, Object> systemCountByProvince() throws Exception;
    /**
     *  还款方式统计
     * @param
     * @return Map<String,Object>
     * @throws
     * @author Vic Tang
     * @date 2018/8/1 20:00
     * */
    Map<String, Object> systemRepaySourceCount() throws Exception;
    /**
     *  近15天统计
     * @param
     * @return Map<String,Object>
     * @throws
     * @author Vic Tang
     * @date 2018/8/1 20:00
     * */
    Map<String, Object> systemFifteenDaysSourceCount() throws Exception;
}
