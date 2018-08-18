package com.adpanshi.cashloan.manage.arc.service.impl;


import com.adpanshi.cashloan.manage.arc.model.SysRoleExample;
import com.adpanshi.cashloan.manage.arc.service.SysRoleService;
import com.adpanshi.cashloan.manage.arc.mapper.SysRoleMapper;
import com.adpanshi.cashloan.manage.arc.model.SysRole;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 系统角色serviceImpl
 * @date 2018/7/31 19:39
 */
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {
    public static final Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);
    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public SysRole getRoleById(Long roleId) {
        return sysRoleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public List<SysRole> getRoleListByUserId(Long userId) {
        return sysRoleMapper.getRoleListByUserId(userId);
    }

    @Override
    public List<SysRole> getList(Map<String, Object> paramMap) {
        return sysRoleMapper.listSelective(paramMap);
    }

    @Override
    public Page<SysRole> getList(Map<String, Object> paramMap, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<SysRole>)sysRoleMapper.listSelective(paramMap);
    }

    @Override
    public int updateState(Long id, Integer isDelete) {
        SysRole sysRole = new SysRole();
        sysRole.setId(id);
        sysRole.setIsDelete(Byte.parseByte(isDelete.toString()));
        return sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }

    @Override
    public int updateRole(AuthUserRole loginUser, SysRole sysRole) {
        sysRole.setUpdateTime(new Date());
        sysRole.setUpdateUser(loginUser.getUserName());
        return sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }

    @Override
    public boolean isExistByNid(String nid) {
        SysRoleExample example = new SysRoleExample();
        example.createCriteria().andNidEqualTo(nid);
        return sysRoleMapper.selectByExample(example).size()>0;
    }

    @Override
    public int addRole(AuthUserRole loginUser, SysRole sysRole) {
        sysRole.setAddTime(new Date());
        sysRole.setAddUser(loginUser.getUserName());
        sysRole.setUpdateTime(new Date());
        sysRole.setUpdateUser(loginUser.getUserName());
        if(sysRole.getIsDelete() == null ){
            sysRole.setIsDelete((byte) 0);
        }
        return this.sysRoleMapper.insertSelective(sysRole);
    }
}
