package com.adpanshi.cashloan.manage.arc.service;


import com.adpanshi.cashloan.manage.arc.model.SysUser;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 系统用户service
 * @date 2018/8/1 15:29
 */
public interface SysUserService {
    /**
     *  修改用户信息
     * @param sysUser
     * @return void
     * @throws
     * @author Vic Tang
     * @date 2018/8/1 16:38
     * */
    void editUser(SysUser sysUser);
    /**
     *  根据roleName查询用户信息
     * @param params
     * @return List<Map<String, Object>>
     * @throws
     * @author Vic Tang
     * @date 2018/8/1 20:53
     * */
    List<Map<String, Object>> getUserInfo(Map<String, Object> params);
    /**
     * @param param
     * @return List<SysUser>
     * @description 获取用户名称
     */
    List<SysUser> getUserName(Map<String, Object> param);

    /**
     * @param id
     * @return int
     * @auther huangqin
     * @description 判断用户是否还在使用这个角色
     * @data 2017-12-19
     */
    int queryRoleUserIsUse(Long id);
    /**
     * @auther huangqin
     * @description 查询用户信息
     * @param currentPage
     * @param pageSize
     * @param params
     * @return age<Map<String, Object>>
     * @data 2017-12-20
     */
    Page<Map<String, Object>> getUserPageList(int currentPage, int pageSize, Map<String, Object> params) ;
    /**
     * @auther huangqin
     * @description 根据工号查询SysUsers
     * @param number
     * @return SysUser
     * @data 2017-12-20
     */
    SysUser getUserByNumber(String number) ;

    /**
     * 根据用户名查询用户信息
     * @param userName 用户名
     * @return SysUser
     */
    SysUser getUserByUserName(String userName) ;

    /**
     * @auther huangqin
     * @description 新增一个用户
     * @param userinfo
     * @param sysUser
     * @param roleIds
     * @return
     * @data 2017-12-20
     */
    void addUser(AuthUserRole userinfo, SysUser sysUser, Long... roleIds) ;
    /**
     * @auther huangqin
     * @description 根据id找到SysUser
     * @param id
     * @return SysUser
     * @data 2017-12-20
     */
    SysUser getUserById(Long id);
    /**
     * @param userinfo
     * @param sysUser
     * @param roleIds
     * @return Boolean
     * @auther huangqin
     * @description 更新用户信息及对应的角色信息
     * @data 2017-12-19
     */
    boolean updateSysUserById(AuthUserRole userinfo,SysUser sysUser,Long... roleIds) ;

    /**
     * @auther huangqin
     * @description 修改用户
     * @param user
     * @return int
     * @data 2017-12-20
     */
    int userUpdate(SysUser user) ;
}
