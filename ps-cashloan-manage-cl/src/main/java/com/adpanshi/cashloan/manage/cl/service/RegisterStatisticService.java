package com.adpanshi.cashloan.manage.cl.service;

import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 注册统计service
 * @date 2018/8/22 17:25
 */
public interface RegisterStatisticService {
    /**
     *  查看用户注册信息分布情况
     * @param time
     * @author Vic Tang
     * @date 2018/8/22 17:43
     * */
    Map<String,Object> getStatisticByDistribution(String time);
    /**
     *  查看范围内用户认证比例
     * @param time
     * @param range
     * @throws
     * @author Vic Tang
     * @date 2018/8/22 21:07
     * */
    Map<String,Object> getStatisticByAuth(String time, String range);
    /**
     *  获取统计截止时间统计报表首页注册与认证信息
     * @param time
     * @return java.util.Map
     * @throws
     * @author Vic Tang
     * @date 2018/8/27 11:46
     * */
    Map<String,Object> getStatisticIndex(String time);
}
