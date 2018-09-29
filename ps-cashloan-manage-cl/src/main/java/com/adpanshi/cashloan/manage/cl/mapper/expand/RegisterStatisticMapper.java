package com.adpanshi.cashloan.manage.cl.mapper.expand;


import com.adpanshi.cashloan.common.mapper.RDBatisDao;
import com.adpanshi.cashloan.manage.cl.pojo.RegisterAuthStatistic;
import com.adpanshi.cashloan.manage.cl.pojo.RegisterStatistic;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 注册统计mapper
 * @date 2018/8/22 17:18
 */
@RDBatisDao
public interface RegisterStatisticMapper {
    /**
     *  查看注册用户按年龄分布统计
     * @param time
     * @return RegisterStatistic
     * @author Vic Tang
     * @date 2018/8/22 17:53
     * */
    List<RegisterStatistic> getStatisticByAge(String time);
    /**
     *  查看注册用户按性别分布统计
     * @param time
     * @return RegisterStatistic
     * @author Vic Tang
     * @date 2018/8/22 17:53
     * */
    List<RegisterStatistic> getStatisticByGender(String time);
    /**
     *  查看范围内用户认证比例
     * @param time
     * @param range
     * @throws
     * @author Vic Tang
     * @date 2018/8/22 21:07
     * */
    List<RegisterAuthStatistic> getStatisticByAuth(@Param("time") String time, @Param("range") String range);
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
