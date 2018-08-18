package com.adpanshi.cashloan.manage.controller;

import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.manage.arc.model.SysRole;
import com.adpanshi.cashloan.manage.arc.model.SysUser;
import com.adpanshi.cashloan.manage.arc.service.*;
import com.adpanshi.cashloan.manage.core.common.enums.AuthCookieEnum;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import com.adpanshi.cashloan.manage.core.common.token.TokenManager;
import com.adpanshi.cashloan.manage.core.common.token.UserCookie;
import com.adpanshi.cashloan.manage.core.common.util.ListUtil;
import com.adpanshi.cashloan.manage.core.common.util.code.MD5;
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
 * @Description: 系统用户controller
 * @date 2018/8/1 15:44
 */
@Scope("prototype")
@Controller
@RequestMapping("system/user/")
public class SysUserController extends ManageBaseController{
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysUserRoleService sysUserRoleService;
    @Resource
    private SignInOutService signInOutService;
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private TokenManager manager;

    /**
     * 修改密码
     *
     * @param user
     * @throws Exception
     */
    @RequestMapping(value = "modifyPassword.htm",method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> modifyPassword(@RequestParam(value = "user") String user) throws Exception {
        Map<String, Object> userMap = parseToMap(user, false);
        SysUser sysUser = this.getLoginUser();
        //SysUser为空则返回Cookie失效
        if (null == sysUser) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.NOCOOKIE_CODE_VALUE), HttpStatus.OK);
        }
        String addTime = String.valueOf(sysUser.getAddTime().getTime());
        String oldPassword = MD5.encode(addTime+String.valueOf(userMap.get("oldPassword")));
        String newPassword1 = MD5.encode(addTime+String.valueOf(userMap.get("newPassword")));
        String newPassword2 = MD5.encode(addTime+String.valueOf(userMap.get("newPassword2")));
        logger.info("原始密码" + sysUser.getPassword());
        logger.info("旧密码" + oldPassword);
//        Map<String,Object>param=new HashMap<>();
//        param.put("userId",sysUser.getId());
//        param.put("roleId",1);
        //所有包含系统管理员的用户修改密码必须在12-24位之间
        if(null != sysUserRoleService.getSysUserRoleList(sysUser.getId(),1L)&&(String.valueOf(userMap.get("newPassword")).length()<12||String.valueOf(userMap.get("newPassword")).length()>24)){
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MODIFY_PASSWORD_SYSTEM_LENGTH), HttpStatus.OK);
        } else if (!sysUser.getPassword().equals(oldPassword)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MODIFY_PASSWORD_OLD_ERROR_CODE_VALUE), HttpStatus.OK);
        } else if (!newPassword1.equals(newPassword2)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MODIFY_PASSWORD_COMPARE_DIFFERENT_CODE_VALUE), HttpStatus.OK);
        } else if (oldPassword.equals(newPassword1)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MODIFY_PASSWORD_CANT_SAME_CODE_VALUE), HttpStatus.OK);
        } else {
            sysUser.setPassword(newPassword1);// 密码加密
            sysUserService.editUser(sysUser);
            //做登出，清楚缓存和Cookie,让用户重新登录
            String token = manager.getToken(request);
            signInOutService.platformSignOut(token);
            response.addCookie(UserCookie.CookieClear(AuthCookieEnum.M_USER_NAME.cookieKey()));
            response.addCookie(UserCookie.CookieClear(AuthCookieEnum.M_TOKEN.cookieKey()));
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * 获取用户所有角色信息
     */
    @RequestMapping(value = "userRoleFind.htm", method = RequestMethod.POST)
    public ResponseEntity<ResultModel> findUser() throws Exception {
        Map<String, Object> responsemap = new HashMap<String, Object>();
        AuthUserRole sysUser = getAuthUserRole();
        if (null == sysUser) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.NOCOOKIE_CODE_VALUE), HttpStatus.OK);
        }
        List<SysRole> roleList = sysRoleService.getRoleListByUserId(sysUser.getUserId());
        responsemap.put("name", sysUser.getRealName());
        responsemap.put("roleList", roleList);
        return new ResponseEntity<>(ResultModel.ok(responsemap), HttpStatus.OK);
    }

    /**
     * 根据角色查询菜单
     * @param sysType
     * @throws Exception
     */
    @RequestMapping(value = "roleMenuFind.htm", method = {RequestMethod.GET})
    public ResponseEntity<ResultModel> fetchRoleMenu(@RequestParam(value = "sysType") String sysType) throws Exception {
        SysRole sysRole = getRoleForLoginUser();
        if (sysType == null) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        } else if (!sysType.matches("[\\d,]+")) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        if (null == sysRole || null == sysRole.getId() || 0L >= sysRole.getId()) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.NOCOOKIE_CODE_VALUE), HttpStatus.OK);
        }
        List<Map<String, Object>> menus = sysMenuService.fetchRoleMenus(
                sysType, sysRole.getId());
        menus = ListUtil.treeForExt(menus, null, null, false);
        return new ResponseEntity<>(ResultModel.ok(menus), HttpStatus.OK);
    }
}
