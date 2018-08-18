package com.adpanshi.cashloan.manage.controller;

import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.common.exception.ManageException;
import com.adpanshi.cashloan.manage.arc.model.SysUser;
import com.adpanshi.cashloan.manage.arc.model.SysUserRole;
import com.adpanshi.cashloan.manage.arc.service.SignInOutService;
import com.adpanshi.cashloan.manage.arc.service.SysUserRoleService;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.enums.AuthCookieEnum;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import com.adpanshi.cashloan.manage.core.common.pojo.TokenModel;
import com.adpanshi.cashloan.manage.core.common.token.TokenManager;
import com.adpanshi.cashloan.manage.core.common.token.UserCookie;
import com.adpanshi.cashloan.manage.pojo.ResultModel;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 登录controller
 * @date 2018/8/1 16:12
 */
@Controller
@Scope("prototype")
@RequestMapping("/system/user/")
public class SysLoginController extends ManageBaseController {
    @Resource
    private SysUserRoleService sysUserRoleService;
    @Resource
    private SignInOutService signInOutService;
    @Resource
    private TokenManager manager;
    /**
     * @param username
     * @param password
     * @return ResponseEntity<ResultModel>
     * @description 用户登录
     * @ModiFyAuther huangqin
     * @ModifyData 2017-12-15
     */
    @RequestMapping(value = "login.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> loginAjax(@RequestParam(value = "username") String username,
                                                 @RequestParam(value = "password") String password) throws Exception {
        TokenModel model = null;
        try {
            //获取登录用户并校验密码
            SysUser sysUser = signInOutService.platformSignIn(username, password.trim());
            //判断是否已经登陆
            if (!signInOutService.checkLogIn(sysUser.getId())) {
                //已经登录则返回是否确认登录
                throw new ManageException(ManageExceptionEnum.LOGIN_CONFIRM_RELOGIN_CODE_VALUE);
            }
            //获取权限列表
            List<SysUserRole> list = sysUserRoleService.getSysUserRoleList(sysUser.getId(),null);
            if (list != null && list.size() > 0) {
                Long roleId = list.get(0).getRoleId();
                //创建token
                model = signInOutService.createToken(sysUser, roleId);
            } else {
                throw new ManageException(ManageExceptionEnum.LOGIN_ERROR_NO_ROLE_CODE_VALUE);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw new ManageException(ManageExceptionEnum.LOGIN_ERROR_CODE_VALUE);
        }
        if (null != model) {
            //写入Cookie
            response.addCookie(UserCookie.CookieAdd(AuthCookieEnum.M_USER_NAME.cookieKey(), model.getUsername()));
            response.addCookie(UserCookie.CookieAdd(AuthCookieEnum.M_TOKEN.cookieKey(), model.getToken()));
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * /**
     *
     * @param username
     * @param password
     * @return ResponseEntity<ResultModel>
     * @description 用户登录确认
     * @ModiFyAuther huangqin
     * @ModifyData 2017-12-15
     */
    @RequestMapping(value = "confirmLogin.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> confirmLogin(@RequestParam(value = "username") String username,
                                                    @RequestParam(value = "password") String password) throws Exception {
        Map<String, Object> res = new HashMap<String, Object>();
        TokenModel model = null;
        try {
            //获取登录用户并校验密码
            SysUser sysUser = signInOutService.platformSignIn(username, password.trim());
            //获取权限列表
            List<SysUserRole> list = sysUserRoleService.getSysUserRoleList(sysUser.getId(),null);
            if (list != null && list.size() > 0) {
                Long roleId = list.get(0).getRoleId();
                //删除之前的token
                signInOutService.removeUserToken(sysUser.getId());
                //创建token
                model = signInOutService.createToken(sysUser, roleId);
                res.put(Constant.RESPONSE_CODE, ManageExceptionEnum.SUCCEED_CODE_VALUE.Code());
                res.put(Constant.RESPONSE_CODE_MSG, "登陆成功！");
            } else {
                throw new ManageException(ManageExceptionEnum.LOGIN_ERROR_NO_ROLE_CODE_VALUE);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw new ManageException(ManageExceptionEnum.LOGIN_ERROR_CODE_VALUE);
        }
        if (null != model) {
            //写入Cookie
            response.addCookie(UserCookie.CookieAdd(AuthCookieEnum.M_USER_NAME.cookieKey(), model.getUsername()));
            response.addCookie(UserCookie.CookieAdd(AuthCookieEnum.M_TOKEN.cookieKey(), model.getToken()));
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * @return ResponseEntity<ResultModel>
     * @description 用户注销/登出
     * @ModiFyAuther huangqin
     * @ModifyData 2017-12-15
     */
    @RequestMapping(value = "logout.htm",method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> logout() {
        String token = manager.getToken(request);
        signInOutService.platformSignOut(token);
        response.addCookie(UserCookie.CookieClear(AuthCookieEnum.M_USER_NAME.cookieKey()));
        response.addCookie(UserCookie.CookieClear(AuthCookieEnum.M_TOKEN.cookieKey()));
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * 切换角色
     *
     * @param role
     * @return ResponseEntity<ResultModel>
     * @throws Exception
     * @description
     * @since 1.0.0
     */
    @RequestMapping(value = "switch/role.htm",method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> changeLoginajax(@RequestParam(value = "role") Long role) throws Exception {
        String token = manager.getToken(request);
        AuthUserRole authUserRole = manager.getAuthUserRole(token);
        if (null == authUserRole) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.NOCOOKIE_CODE_VALUE), HttpStatus.OK);
        } else {
            authUserRole.setRoleId(role);
            manager.deleteToken(token);
            TokenModel model = manager.createToken(authUserRole);
            response.addCookie(UserCookie.CookieAdd(AuthCookieEnum.M_TOKEN.cookieKey(), model.getToken()));
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }
}
