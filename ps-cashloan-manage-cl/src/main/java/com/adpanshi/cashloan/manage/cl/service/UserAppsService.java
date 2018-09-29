package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.common.exception.ServiceException;
import com.adpanshi.cashloan.manage.cl.model.UserApps;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 用户App信息service
 * @date 2018/8/2 20:12
 */
public interface UserAppsService {
    /**
     *  获取用户app信息
     * @param userId,
     * @param current
     * @param pageSize
     * @return Page<UserApps>
     * @throws
     * @author Vic Tang
     * @date 2018/8/6 11:13
     * */
    Page<UserApps> getUserAppsPageList(Integer current, Integer pageSize, Long userId) throws ServiceException;
    /**
     * <p>根据给定的条件查询</p>
     * @param userId 用户id(根据userId构造表名)
     * @param userApps
     * @return List<UserApps>
     * */
    List<UserApps> listSelective(Long userId,Map<String,Object> userApps);
}
