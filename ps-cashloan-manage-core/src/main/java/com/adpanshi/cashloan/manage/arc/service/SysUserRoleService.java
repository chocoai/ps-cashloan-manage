package com.adpanshi.cashloan.manage.arc.service;


import com.adpanshi.cashloan.manage.arc.model.SysUserRole;

import java.util.List;

/**
 * @author Vic Tang
 * @Description: 系统用户角色service
 * @date 2018/8/1 15:34
 */
public interface SysUserRoleService {
    /**
     *  获取用户角色
     * @param userId
     * @param roleId
     * @return List<SysUserRole>
     * @throws
     * @author Vic Tang
     * @date 2018/8/1 15:54
     * */
    List<SysUserRole> getSysUserRoleList(Long userId,Long roleId);
}
