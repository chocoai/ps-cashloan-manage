package com.adpanshi.cashloan.manage.arc.service.impl;



import com.adpanshi.cashloan.manage.arc.mapper.SysUserRoleMapper;
import com.adpanshi.cashloan.manage.arc.model.SysUserRole;
import com.adpanshi.cashloan.manage.arc.model.SysUserRoleExample;
import com.adpanshi.cashloan.manage.arc.service.SysUserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Vic Tang
 * @Description: 系统用户角色serviceImpl
 * @date 2018/8/1 15:36
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl implements SysUserRoleService{
    public static final Logger logger = LoggerFactory.getLogger(SysUserRoleServiceImpl.class);

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    public List<SysUserRole> getSysUserRoleList(Long userId,Long roleId) {
        SysUserRoleExample example = new SysUserRoleExample();
        SysUserRoleExample.Criteria criteria = example.createCriteria();
        if(userId != null){
            criteria.andUserIdEqualTo(userId);
        }
        if(roleId != null){
            criteria.andRoleIdEqualTo(roleId);
        }
        return sysUserRoleMapper.selectByExample(example);
    }
}
