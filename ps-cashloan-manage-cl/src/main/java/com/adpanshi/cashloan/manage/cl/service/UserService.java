package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.User;
import com.adpanshi.cashloan.manage.cl.model.expand.UserBaseInfoModel;
import com.adpanshi.cashloan.manage.cl.pojo.AuthUserModel;
import com.github.pagehelper.Page;

import java.util.Map;

/**
 * @author Vic Tang
 * @Description: app用户service
 * @date 2018/8/3 11:15
 */
public interface UserService {
    /**
     *  根据主键获取用户信息
     * @param id
     * @return User
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 11:17
     * */
    User getById(Long id);
    /**
     * 查询用户详细信息列表
     * @param params
     * @param currentPage
     * @param pageSize
     * @return
     */
    Page<UserBaseInfoModel> listUser(Map<String, Object> params, Integer currentPage, Integer pageSize);
    /**
     * 查询复借用户详细信息列表zy 2017.7.26
     * @param params
     * @param currentPage
     * @param pageSize
     * @return
     */
    Page<UserBaseInfoModel> listReUser(Map<String, Object> params, Integer currentPage, Integer pageSize);
    /**
     * 查询用户详细信息列表
     * @param params
     * @param currentPage
     * @param pageSize
     * @return
     */
    Page<AuthUserModel> listAuthUser(Map<String, Object> params, Integer currentPage, Integer pageSize);
}
