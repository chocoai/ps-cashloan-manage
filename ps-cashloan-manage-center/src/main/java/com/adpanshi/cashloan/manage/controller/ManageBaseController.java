package com.adpanshi.cashloan.manage.controller;


import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.common.exception.ManageException;
import com.adpanshi.cashloan.common.exception.ServiceException;
import com.adpanshi.cashloan.manage.arc.model.SysRole;
import com.adpanshi.cashloan.manage.arc.model.SysUser;
import com.adpanshi.cashloan.manage.core.common.util.JsonUtil;
import com.adpanshi.cashloan.manage.arc.service.SysRoleService;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import com.adpanshi.cashloan.manage.core.common.token.TokenManager;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基类action
 *
 * @version 1.0
 * @created 2017年12月18日 下午22:11:21
 */
@Controller
public abstract class ManageBaseController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @Resource
    protected SysRoleService roleService;
    @Resource
    protected TokenManager manager;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }

    /**
     * @return SysUser
     * @auther huangqin
     * @description 获取当前登录用户的用户
     * @data 2017-12-16
     */
    protected SysUser getLoginUser() {
        SysUser sysUser = manager.getSysUser(manager.getToken(request));
        if (sysUser != null) {
            return sysUser;
        }
        return null;
    }

    /**
     * @return AuthUserRole
     * @auther huangqin
     * @description 获取当前登录用户的自定义数据
     * @data 2017-12-16
     */
    protected AuthUserRole getAuthUserRole() {
        String token = manager.getToken(request);
        AuthUserRole authUserRole = manager.getAuthUserRole(token);
        if (authUserRole != null) {
            return authUserRole;
        }
        return null;
    }


    /**
     * @return SysRole
     * @auther huangqin
     * @description 获取当前登录用户的角色信息
     * @data 2017-12-16
     */
    public SysRole getRoleForLoginUser() throws ServiceException {
        AuthUserRole authUserRole = getAuthUserRole();
        Long roleId = authUserRole.getRoleId();
        SysRole role = roleService.getRoleById(roleId);
        return role;

    }

    /**
     * @return List<Long>
     * @auther huangqin
     * @description 获取当前登录用户的用户
     * @data 2017-12-16
     */
    public List<Long> getRole() {
        List<Long> roles = new ArrayList<Long>();
        AuthUserRole authUserRole = getAuthUserRole();
        roles.add(authUserRole.getRoleId());
        return roles;
    }

    /**
     * @auther huangqin
     * @description 将Sring类型的数据转成Map
     * @param data
     * @param canEmpty 是否可以为空 true: 可以为空; false: 不可以为空,抛出异常
     * @return Map
     * @data 2017-12-20
     */
    public static Map<String, Object> parseToMap(String data, boolean canEmpty )throws ManageException {
        Map<String, Object> map = new HashMap<>();
        parseTo(data,"Map",canEmpty,null,map,null);
        return map;
    }

    /**
     * @auther huangqin
     * @description 将Sring类型的数据转成List
     * @param data
     * @param canEmpty 是否可以为空 true: 可以为空; false: 不可以为空,抛出异常
     * @return List
     * @data 2017-12-20
     */
    public static List parseToList(String data,boolean canEmpty )throws ManageException{
        List list = null;
        parseTo(data,"List",canEmpty,null,null,list);
        return list;
    }

    /**
     * @auther huangqin
     * @description 将Sring类型的数据转成Bean对象
     * @param data
     * @param clz
     * @param canEmpty 是否可以为空 true: 可以为空; false: 不可以为空,抛出异常
     * @return T
     * @data 2017-12-21
     */
    public static <T> T parseToBean(String data, Class<T> clz,boolean canEmpty )throws ManageException {
        return parseTo(data,"Pojo",canEmpty,clz,null,null);
    }

    /**
     * @auther nmnl
     * @description 将String 转换 Map,List
     * @param data
     * @param classType
     * @param map
     * @param list
     * @param canEmpty 是否可以为空 true: 可以为空; false: 不可以为空,抛出异常
     * @data 2018-1-10
     */
    private static <T> T parseTo (String data, String classType,boolean canEmpty,Class<T> clz, Map<String, Object> map , List list)throws ManageException{
        if (StringUtils.isNotEmpty(data)) {
            if("Map".equals(classType)){
                Map<String, Object> objMap = JsonUtil.parse(data, Map.class);
                if (null != objMap && objMap.size() > 0){
                    // 集合不为空则开始递归去除字符串两端的空格
                    for(Map.Entry<String, Object> entry : objMap.entrySet()) {
                        if(null != objMap.get(entry.getKey()) && !"".equals(objMap.get(entry.getKey()))) {
                            map.put(entry.getKey(), objMap.get(entry.getKey()).toString().trim());
                        }
                    }
                }
            }
            if("List".equals(classType)){
                List objList = JSONObject.parseArray(data, List.class);
                for (int i = 0; i < objList.size(); i++){
                    list.add(objList.get(i));
                }
            }
            if("Pojo".equals(classType)){
                return JsonUtil.parse(data, clz);
            }
        }else {
            //是否可以为空，不可以则抛出异常
            if(!canEmpty){
                throw new ManageException(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT);
            }
        }
        return null;
    }

}
