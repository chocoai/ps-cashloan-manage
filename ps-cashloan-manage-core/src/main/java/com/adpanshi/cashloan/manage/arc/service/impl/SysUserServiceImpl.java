package com.adpanshi.cashloan.manage.arc.service.impl;



import com.adpanshi.cashloan.manage.arc.mapper.SysRoleMapper;
import com.adpanshi.cashloan.manage.arc.mapper.SysUserMapper;
import com.adpanshi.cashloan.manage.arc.mapper.SysUserRoleMapper;
import com.adpanshi.cashloan.manage.arc.model.SysUser;
import com.adpanshi.cashloan.manage.arc.model.SysUserExample;
import com.adpanshi.cashloan.manage.arc.model.SysUserRole;
import com.adpanshi.cashloan.manage.arc.model.SysUserRoleExample;
import com.adpanshi.cashloan.manage.arc.service.SysUserService;
import com.adpanshi.cashloan.manage.core.common.context.SystemConstant;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import com.adpanshi.cashloan.manage.core.common.util.code.MD5;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 系统用户serviceImpl
 * @date 2018/8/1 15:31
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService{
    public static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Override
    public void editUser(SysUser sysUser) {
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public List<Map<String, Object>> getUserInfo(Map<String, Object> params) {
        return sysUserMapper.getUserInfo(params);
    }

    @Override
    public List<SysUser> getUserName(Map<String, Object> param) {
        return sysUserMapper.getSysUserByMap(param);
    }

    @Override
    public int queryRoleUserIsUse(Long id) {
        Map<String, Object> param = new HashMap<>();
        param.put("roleId", id.longValue());
        return sysUserMapper.queryRoleUserIsUse(param);
    }

    @Override
    public Page<Map<String, Object>> getUserPageList(int currentPage, int pageSize, Map<String, Object> params)  {
        PageHelper.startPage(currentPage, pageSize);
        return (Page<Map<String, Object>>) sysUserMapper.listUserInfo(params);
    }

    @Override
    public SysUser getUserByNumber(String number) {
        SysUserExample example = new SysUserExample();
        example.createCriteria().andJobNumEqualTo(number);
//        List<SysUser> infos = sysUserMapper.selectByExample(example);
//        if(infos.size() > 0){
//            return infos.get(0);
//        } else {
//            return null;
//        }
        return sysUserMapper.selectByExample(example).get(0);
    }

    @Override
    public SysUser getUserByUserName(String userName) {
        SysUserExample example = new SysUserExample();
        example.createCriteria().andUserNameEqualTo(userName);
        List<SysUser> infos = sysUserMapper.selectByExample(example);
        if(infos.size() > 0){
            return infos.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void addUser(AuthUserRole userinfo, SysUser sysUser, Long... roleIds) {
        //添加时间
        Date curDate = new Date();
        //因为从数据库取AddTime时，没有毫秒所以我们在存的时候就把毫秒去掉
        //为防止在存入数据库时，毫秒值会四舍五入影响到秒，在存入之前就去掉毫秒
        curDate = new Date(curDate.getTime()/1000*1000);
        //添加人
        String loginUserName = null != userinfo ? userinfo.getRealName() :"";
        //职位
        sysUser.setPosition(0);
        sysUser.setAddTime(curDate);
        sysUser.setAddUser(loginUserName);
        sysUser.setUpdateTime(curDate);
        sysUser.setUpdateUser(loginUserName);
        // 账号初始密码
        sysUser.setPassword(MD5.encode(curDate.getTime()+ SystemConstant.SYSTEM_PASSWORD_DEFAULT));
        // 用户状态：正常
        sysUser.setStatus(SystemConstant.USER_STATUS_NORMAL);
        // 增加用户
        sysUserMapper.insertSelective(sysUser);
        //保存用户角色
        for (int i = 0; i < roleIds.length; i++) {
            Long role = roleIds[i];
            // 增加用户角色关系
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(role);
            sysUserRole.setUserId(sysUser.getId());
            sysUserRoleMapper.insertSelective(sysUserRole);
        }
    }

    @Override
    public SysUser getUserById(Long id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateSysUserById(AuthUserRole userinfo, SysUser sysUser, Long... roleIds) {
        //首先删除角色关系
        Long userId = sysUser.getId();
        SysUserRoleExample example = new SysUserRoleExample();
        example.createCriteria().andUserIdEqualTo(userId);
        sysUserRoleMapper.deleteByExample(example);
        for (int i = 0; i < roleIds.length; i++) {
            Long roleId = roleIds[i];
            // 增加用户角色关系
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(roleId.longValue());
            sysUserRole.setUserId(userId.longValue());
            sysUserRoleMapper.insertSelective(sysUserRole);
        }
        return sysUserMapper.updateByPrimaryKeySelective(sysUser) >0;
    }

    @Override
    public int userUpdate(SysUser user) {
        return sysUserMapper.updateByPrimaryKeySelective(user);
    }
}
