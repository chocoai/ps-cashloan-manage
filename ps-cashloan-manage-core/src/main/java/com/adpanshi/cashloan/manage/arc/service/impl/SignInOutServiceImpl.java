package com.adpanshi.cashloan.manage.arc.service.impl;


import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.common.exception.ManageException;
import com.adpanshi.cashloan.manage.arc.mapper.SysRoleMapper;
import com.adpanshi.cashloan.manage.arc.mapper.SysUserMapper;
import com.adpanshi.cashloan.manage.arc.model.SysRole;
import com.adpanshi.cashloan.manage.arc.model.SysUser;
import com.adpanshi.cashloan.manage.arc.model.SysUserExample;
import com.adpanshi.cashloan.manage.arc.service.SignInOutService;
import com.adpanshi.cashloan.manage.core.common.enums.RedisCachePrefixEnum;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import com.adpanshi.cashloan.manage.core.common.pojo.TokenModel;
import com.adpanshi.cashloan.manage.core.common.token.TokenManager;
import com.adpanshi.cashloan.manage.core.common.util.code.MD5;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;


@Service("signInOutServiceImpl")
public class SignInOutServiceImpl implements SignInOutService {

    protected Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Autowired
    TokenManager manager;

    /**
     * 登录
     * 完善.
     * 1.平台用户 (管理员类型)
     *
     * @author M
     * @date 20170309
     */
    @Override
    public SysUser platformSignIn(String username, String password) throws Exception {
        log.info("用户登录身份验证:" + username);
        SysUserExample example = new SysUserExample();
        example.createCriteria().andUserNameEqualTo(username);
        List<SysUser> infos = sysUserMapper.selectByExample(example);
        if (infos.size() > 0) {
            SysUser sysUser = infos.get(0);
            //用户状态不为正常.则失效
            //状态：0正常 1禁用
            if (sysUser.getStatus() == 0) {
                String addTime = String.valueOf(sysUser.getAddTime().getTime());
                String loginPassword = MD5.encode(addTime + password);
                //校验用户密码
                String pw = sysUser.getPassword();
                if (pw.equals(loginPassword)) {
                    return sysUser;
                }
                log.error("***************userName="+username+"; password="+password+"; addTime="+addTime+"; loginPassword="+loginPassword+"; localPassword="+sysUser.getPassword());
                //提示用户名或密码错误 密码错误的情况
                throw new ManageException(ManageExceptionEnum.LOGIN_ERROR_PASSWORD_ERROR_CODE_VALUE);
            }
            //提示用户停用的情况
            throw new ManageException(ManageExceptionEnum.LOGIN_ERROR_USER_LOCKED_CODE_VALUE);
        }
        // 没找到帐号
        throw new ManageException(ManageExceptionEnum.LOGIN_ERROR_NO_COUNT_CODE_VALUE);
    }

    @Override
    public TokenModel createToken(SysUser sysUser, Long roleId) throws Exception {
        log.info("创建用户token:" + sysUser.getUserName());
        SysRole role = sysRoleMapper.selectByPrimaryKey(roleId.longValue());
        AuthUserRole aur = new AuthUserRole();
        aur.setUserId(sysUser.getId());
        aur.setUserName(sysUser.getUserName());
        aur.setRealName(sysUser.getName());
        aur.setRoleId(roleId);
        aur.setRoleName(null == role ? "" : role.getName());
        aur.setOfficeId(sysUser.getOfficeId());
        aur.setPhone(sysUser.getPhone());
        aur.setMobile(sysUser.getMobile());
        aur.setJobNum(sysUser.getJobNum());
        aur.setStatus(sysUser.getStatus().intValue());
        return manager.createToken(aur);
    }

    @Override
    public void platformSignOut(String token) {
        log.info("cookie 存的token:" + token);
        manager.deleteToken(token);
    }

    @Override
    public boolean checkLogIn(Long userId) {
        Set<String> userKeys = manager.getKeys(RedisCachePrefixEnum.TokenCache.prefix() + "*");
        for (String userKsy : userKeys) {
            AuthUserRole suthUserRole = manager.getAuthUserRoleOnly
                    (userKsy.substring(userKsy.indexOf(RedisCachePrefixEnum.TokenCache.prefix()) + RedisCachePrefixEnum.TokenCache.prefix().length()));
            if (null != suthUserRole) {
                if (userId.longValue() == suthUserRole.getUserId().longValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param userId
     * @auther huangqin
     * @description 删除当前用户所存的所有token缓存
     * @data 2017-12-19
     */
    @Override
    public void removeUserToken(Long userId) {
        String logInkey = "";
        Set<String> userKeys = manager.getKeys(RedisCachePrefixEnum.TokenCache.prefix() + "*");
        for (String userKsy : userKeys) {
            logInkey = userKsy.substring(userKsy.indexOf(RedisCachePrefixEnum.TokenCache.prefix()) + RedisCachePrefixEnum.TokenCache.prefix().length());
            AuthUserRole suthUserRole = manager.getAuthUserRoleOnly(logInkey);
            if (null != suthUserRole) {
                if (userId.longValue() == suthUserRole.getUserId().longValue()) {
                    manager.deleteToken(logInkey);
                }
            }
        }
    }
}