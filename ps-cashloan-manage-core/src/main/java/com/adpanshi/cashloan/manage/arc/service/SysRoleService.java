package com.adpanshi.cashloan.manage.arc.service;


import com.adpanshi.cashloan.manage.arc.model.SysRole;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import com.github.pagehelper.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 系统角色service
 * @date 2018/7/31 19:37
 */
public interface SysRoleService {
    /**
     * 根据主键获取角色信息
     * @param roleId
     * @return com.adpanshi.cashloan.manage.arc.pojo.SysRole
     * @throws
     * @author Vic Tang
     * @date 2018/7/31 20:08
     */
    SysRole getRoleById(Long roleId);
    /**
     *  根据userId获取所有角色
     * @param userId
     * @return List<SysRole>
     * @throws
     * @author Vic Tang
     * @date 2018/8/1 16:32
     * */
    List<SysRole> getRoleListByUserId(Long userId);
    /**
     * @param paramMap
     * @return List<SysRole>
     * @auther huangqin
     * @description 查询角色列表
     * @data 2017-12-19
     */
    List<SysRole> getList(Map<String, Object> paramMap);
    /**
     * @param paramMap
     * @return List<SysRole>
     * @auther huangqin
     * @description 查询角色列表
     * @data 2017-12-19
     */
    Page<SysRole> getList(Map<String, Object> paramMap, int current, int pageSize);

    /**
     * @param id
     * @param isDelete
     * @return int
     * @auther huangqin
     * @description 更新角色状态
     * @data 2017-12-19
     */
    int updateState(Long id, Integer isDelete);
    /**
     * @param loginUser
     * @param sysRole
     * @return int
     * @auther huangqin
     * @description 更新角色信息
     * @data 2017-12-19
     */
    int updateRole(AuthUserRole loginUser, SysRole sysRole);
    /**
     * @param nid
     * @return int
     * @auther huangqin
     * @description 根据nid判断是否有重复的用户
     * @data 2017-12-19
     */
    boolean isExistByNid(String nid);
    /**
     * @param loginUser
     * @param sysRole
     * @return int
     * @auther huangqin
     * @description 新增角色信息
     * @data 2017-12-19
     */
    int addRole(AuthUserRole loginUser, SysRole sysRole);
}
