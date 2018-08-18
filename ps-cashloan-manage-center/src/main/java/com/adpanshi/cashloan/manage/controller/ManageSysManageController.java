package com.adpanshi.cashloan.manage.controller;


import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.manage.arc.enums.SysRoleEnum;
import com.adpanshi.cashloan.manage.arc.enums.SysUserEnum;
import com.adpanshi.cashloan.manage.arc.model.*;
import com.adpanshi.cashloan.manage.arc.model.expand.SysConfigModel;
import com.adpanshi.cashloan.manage.arc.service.*;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.context.Global;
import com.adpanshi.cashloan.manage.core.common.context.SystemConstant;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import com.adpanshi.cashloan.manage.core.common.util.*;
import com.adpanshi.cashloan.manage.core.common.util.code.MD5;
import com.adpanshi.cashloan.manage.pojo.ResultModel;
import com.github.pagehelper.Page;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * @version 1.0.0
 * @auther huangqin
 * @data 2017-12-19
 * @dsscription 系统管理Controller
 * @since 2017-12-19 16:16:17
 */
@Scope("prototype")
@Controller
@RequestMapping("/manage/sysManage/")
public class ManageSysManageController extends ManageBaseController {
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private SysDictDetailService sysDictDetailService;


    /******角色管理******/
    /**
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 系统管理-角色管理-角色查询下拉列表查询
     * @data 2017-12-19
     */
    @RequestMapping(value = "roleDropDownList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> roleDropDownList() {
        this.logger.info("------系统管理-角色管理-角色查询下拉列表查询------");
        //读库标志
        Map<String, Object> result = new HashMap<>();
        result.put("roleList", sysRoleService.getList(new HashMap<String, Object>()));
        result.put("roleStateList", SysRoleEnum.ROLE_STATE.EnumValueS());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @param searchParams
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 系统管理-角色管理-查询
     * @data 2017-12-19
     */
    @RequestMapping(value = "roleList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> roleList(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                @RequestParam(value = "current") Integer current,
                                                @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        this.logger.info("------系统管理-角色管理-查询------");
        //读库标志
        Map<String, Object> params = parseToMap(searchParams, true);
        Page<SysRole> page = sysRoleService.getList(params, current, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @param roleId
     * @param state
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 系统管理-角色管理-角色状态变更
     * @data 2017-12-19
     */
    @RequestMapping(value = "roleChangeState.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> roleChangeState(@RequestParam("roleId") Long roleId,
                                                       @RequestParam("state") Integer state) {
        this.logger.info("------系统管理-角色管理-角色状态变更------");
        // 参数校验
        if (null == roleId || null == state) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        //如果是停用操作
        if (1 == state.intValue()) {
            // 判断用户是否还在使用这个角色,若还在使用则不予更改
            if (sysUserService.queryRoleUserIsUse(roleId) >= 1) {
                return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MANAGE_SYS_MANAGE_ROLE_USED_CODE_VALUE), HttpStatus.OK);
            }
        }
        //更新角色状态
        if (sysRoleService.updateState(roleId, state) <= 0) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_UPDATE_CODE_VALUE), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * @param sysRole
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 新增一个角色
     * @data 2017-12-19
     */
    @RequestMapping(value = "roleSaveOrUpdate.htm", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<ResultModel> roleSaveOrUpdate(SysRole sysRole) throws Exception {
        this.logger.info("------系统管理-角色管理-新增------");
        int total = 0;
        //新增/更新角色
        AuthUserRole loginUser = this.getAuthUserRole();
        if (null != sysRole.getId() && StringUtil.isNotEmptys(sysRole.getId())) {
            //更新角色
            total = sysRoleService.updateRole(loginUser,sysRole);
        } else {
            //判断nid是否已经存在
            if (sysRoleService.isExistByNid(sysRole.getNid())) {
                return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MANAGE_SYS_MANAGE_ROLE_EXIST_CODE_VALUE), HttpStatus.OK);
            }
            total = sysRoleService.addRole(loginUser, sysRole);
        }
        if (total <= 0) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_INSERT_CODE_VALUE), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * @param roleId
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 获取当前登录用户的权限树 这个url有什么用??????  这个url是角色分配权限查询的
     * @data 2017-12-19
     */
    @RequestMapping(value = "findRoleMenuHas.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> findRoleMenuHas(@RequestParam(value = "roleId") Long roleId)
            throws Exception {
        this.logger.info("------系统管理-角色管理-分配权限查询------");
        //获取所有菜单，角色拥有的checked为1，没有的checked 为 0
        List<Map<String, Object>> menus = sysMenuService.fetchRoleMenuHas(roleId);
        //获取用户所有的权限，角色拥有的checked为1，没有的checked 为 0
        List<Map<String, Object>> list = sysMenuService.fetchRolebtnHas(roleId);
        //菜单子菜单挂到父菜单下
        menus = ListUtil.list2Tree(menus, "value", "parentId");
        menus = ListUtil.treeForExt(menus, null, null, true);
        for (Map<String, Object> parentMenu : menus) {
            List<Map<String, Object>> menuChilds = ((List<Map<String, Object>>) parentMenu.get("children"));
            if (null != menuChilds) {
                for (Map<String, Object> menuChild : menuChilds) {
                    //把子菜单所有的权限挂到该菜单下
                    List<Map<String, Object>> menuPerms = null;
                    for (Map<String, Object> perm : list) {
                        if ((StringUtil.isNull(perm.get("parentId")).equals(StringUtil.isNull(menuChild.get("value"))))) {
                            if (null == menuPerms) {
                                menuPerms = new ArrayList<>();
                            }
                            menuPerms.add(perm);
                        }
                    }
                    if (null != menuPerms) {
                        menuChild.put("children", menuPerms);
                    }
                    Long checked = Long.parseLong(menuChild.get("checked").toString());
                    //如果子菜单有权限未分配给该角色，则该子菜单选中状态checked从1变为0
                    if (checked.longValue() == 1) {
                        List<Map<String, Object>> childrens = (List<Map<String, Object>>) menuChild.get("children");
                        if (null != childrens && childrens.size() > 0) {
                            for (int i = 0; i < childrens.size(); i++) {
                                Map<String, Object> map = childrens.get(i);
                                Long flag = Long.parseLong(map.get("checked").toString());
                                if (flag.longValue() != 1) {
                                    menuChild.put("checked", 0);
                                    break;
                                }
                            }
                        }
                    }
                    if (Integer.valueOf(String.valueOf(menuChild.get("checked"))) == 0) {
                        parentMenu.put("checked", 0);
                    }
                }
            }
        }
        return new ResponseEntity<>(ResultModel.ok(menus), HttpStatus.OK);
    }

    /**
     * @param menus
     * @param roleId
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 修改角色的权限
     * @data 2017-12-19
     * @remark mysql执行DELETE操作时，并不会立即释放表空间，除非有下一条主键相同的数据被插入否则将一直占用这一段空间
     * 因此需要运维定时清理一下这里面的无用数据
     */
    @RequestMapping(value = "updateRoleMenuHas.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> updateRoleMenuHas(@RequestParam(value = "roleId") Long roleId,
                                                         @RequestParam(value = "menus") Long... menus) throws Exception {
        this.logger.info("------系统管理-角色管理-分配权限保存------");
        if (null == menus || menus.length <= 0) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MANAGE_SYS_MANAGE_REPEATED_NUMBER_CODE_VALUE), HttpStatus.OK);
        }
        if (!sysMenuService.updateRoleMenuHas(roleId, menus)){
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_INSERT_CODE_VALUE), HttpStatus.OK);
        }
        //保存完成后清除redis对应的权限缓存,该缓存会在下次请求时再次存到redis里
        manager.deleteTokenByKey(RedisKey.getManagerMenuByRoleId(roleId));
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }


    /******用户管理******/
    /**
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 系统管理-用户管理-用户查询下拉列表查询
     * @data 2017-12-19
     */
    @RequestMapping(value = "userDropDownList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> userDropDownList() {
        this.logger.info("------系统管理-用户管理-用户查询下拉列表查询------");
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        param.put("isDelete","0");
        result.put("roleList", sysRoleService.getList(param));
        result.put("userStateList", SysUserEnum.SYS_USER_STATE.EnumValueS());
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @param searchParams
     * @param current
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 用户管理查询
     * @data 2017-12-20
     */
    @RequestMapping(value = "userList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> userList(@RequestParam(value = "searchParams", required = false) String searchParams,
                                                @RequestParam(value = "current") Integer current,
                                                @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        this.logger.info("------系统管理-用户管理-查询------");
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = parseToMap(searchParams, true);
        //查询
        Page<Map<String, Object>> page = sysUserService.getUserPageList(current, pageSize, params);
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }


    /**
     * @param sysUser
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 新增用户
     * @data 2017-12-20
     */
    @RequestMapping(value = "userSave.htm", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<ResultModel> userSave(SysUser sysUser, Long... roleIds) throws Exception {
        this.logger.info("------系统管理-用户管理-新增------");
        // 获取当前登录用户信息
        AuthUserRole userinfo = this.getAuthUserRole();
        //工号验证
        if (null != sysUserService.getUserByNumber(sysUser.getJobNum())) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MANAGE_SYS_MANAGE_REPEATED_NUMBER_CODE_VALUE), HttpStatus.OK);
        }
        // 用户名验证
        if (null != sysUserService.getUserByUserName(sysUser.getUserName())) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MANAGE_SYS_MANAGE_REPEATED_USERNAME_CODE_VALUE), HttpStatus.OK);
        }
        sysUserService.addUser(userinfo, sysUser, roleIds);// 增加用户
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * @param sysUser param roleIds
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 编辑用户
     * @data 2017-12-20
     */
    @RequestMapping(value = "userEdit.htm", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<ResultModel> userEdit(SysUser sysUser, Long... roleIds) throws Exception {
        this.logger.info("------系统管理-用户管理-编辑------");
        // 获取当前登录用户信息
        AuthUserRole userinfo = this.getAuthUserRole();
        //更新时验证用户名是否重复
        SysUser user1 = sysUserService.getUserById(sysUser.getId());
        String userName = sysUser.getUserName();
        if (!userName.equals(user1.getUserName())) {
            SysUser user2 = sysUserService.getUserByUserName(userName);
            if (null != user2) {
                return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MANAGE_SYS_MANAGE_REPEATED_USERNAME_CODE_VALUE), HttpStatus.OK);
            }
        }
        // 更新用户信息及对应的角色信息
        if (!sysUserService.updateSysUserById(userinfo, sysUser, roleIds)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_UPDATE_CODE_VALUE), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }


    /**
     * @param id
     * @param status
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 用户 重置密码/锁定/解锁
     * @data 2017-12-20
     */
    @RequestMapping(value = "userUpdate.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> update(@RequestParam(value = "id") Long id,
                                              @RequestParam(value = "status") String status) throws Exception {
        this.logger.info("------系统管理-用户管理-重置密码/锁定/解锁------");
        //参数校验
        if (StringUtil.isEmpty(id) || StringUtil.isEmpty(status)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        SysUser sysUser = sysUserService.getUserById(id);
        if (null == sysUser) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MANAGE_SYS_MANAGE_NO_SYSUSER_CODE_VALUE), HttpStatus.OK);
        }
        if ("lock".equals(status)) {
            sysUser.setStatus((byte) 1);
        } else if ("unLock".equals(status)) {
            sysUser.setStatus((byte) 0);
        } else if ("resetPassword".equals(status)) {
            sysUser.setPassword(MD5.encode(sysUser.getAddTime().getTime()+ SystemConstant.SYSTEM_PASSWORD_DEFAULT));
        }
        sysUser.setUpdateTime(new Date());
        if (sysUserService.userUpdate(sysUser) <= 0) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_UPDATE_CODE_VALUE), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }


    /****** 参数配置 ******/

    /**
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 系统管理-参数配置-下拉列表查询
     * @data 2017-12-19
     */
    @RequestMapping(value = "configDropDownList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> configDropDownList() {
        this.logger.info("------系统管理-参数配置-角色查询下拉列表查询------");
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("typeCode", "SYSTEM_TYPE");
        Map<String, Object> result = new HashMap<>();
        result.put("typeList", sysDictDetailService.listByTypeCode(data));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }


    /**
     * @param current
     * @param pageSize
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 系统参数 查询
     * @data 2017-12-20
     */
    @RequestMapping(value = "configList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> configList(@RequestParam(value = "current") Integer current,
                                                  @RequestParam(value = "pageSize") Integer pageSize,
                                                  @RequestParam(value = "searchParams", required = false) String searchParams) throws Exception {
        this.logger.info("------系统管理-系统参数-查询------");
        Map<String, Object> paramap = parseToMap(searchParams, false);
        List<Map<String, Object>> typeList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        //获取系统参数类型数据字典
        List<Map<String, Object>> dicList = sysDictService.getDictsCache("SYSTEM_TYPE");
        for (Map<String, Object> dic : dicList) {
            Map<String, Object> types = new HashMap<>();
            types.put("systemType", dic.get("value"));
            types.put("systemTypeName", dic.get("text"));
            dataMap.put((String) dic.get("value"), dic.get("text"));
            typeList.add(types);
        }
        //返回页面的json参数
        Page<SysConfig> page = sysConfigService.getSysConfigPageList(current, pageSize, paramap);
        List<SysConfigModel> sysModel = new ArrayList<>();
        if (page != null && !page.isEmpty()) {
            for (SysConfig sys : page) {
                SysConfigModel model = new SysConfigModel();
                model = model.getSysModel(sys, dataMap);
                sysModel.add(model);
            }
        }
        Map<String, Object> returnMap = new HashMap<>();
        //返回给页面
        returnMap.put("dicData", typeList);
        returnMap.put(Constant.RESPONSE_DATA, sysModel);
        returnMap.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(returnMap), HttpStatus.OK);
    }


    /**
     * @param sysConfig
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 系统参数 新增和修改
     * @data 2017-12-20
     */
    @RequestMapping(value = "configSaveOrUpdate.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> saveOrUpdate(SysConfig sysConfig) throws Exception {
        this.logger.info("------系统管理-系统参数-新增/修改------");
        int count = 0;
        if (null != sysConfig.getId() && sysConfig.getId().longValue() > 0) {
            //修改数据
            count = sysConfigService.updateSysConfig(sysConfig);
        } else {
            if(null != sysConfigService.selectByCode(sysConfig.getCode())){
                return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MANAGE_SYS_MANAGE_CONFIG_CODE_EXIST_CODE_VALUE), HttpStatus.OK);
            }
            AuthUserRole sysUser = getAuthUserRole();
            sysConfig.setStatus(1);//新建时有效
            sysConfig.setCreator(null != sysUser ? sysUser.getUserId() : null);
            //动态插入数据
            count = sysConfigService.insertSysConfig(sysConfig);
        }
        //判断操作是否成功
        if (count <= 0) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_UPDATE_CODE_VALUE), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }


    /**
     * @param id
     * @param status
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 系统参数 禁用和启用
     * @data 2017-12-20
     */
    @RequestMapping(value = "configUpdateState.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> configUpdateState(@RequestParam(value = "id") Long id,
                                                         @RequestParam(value = "status") Integer status) throws Exception {
        this.logger.info("------系统管理-系统参数-禁用/启用------");
        //参数校验
        if (StringUtil.isEmpty(id) || null == status) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        SysConfig sysConfig = new SysConfig();
        //修改数据
        sysConfig.setId(id);
        sysConfig.setStatus(status);
        //判断操作是否成功
        if (sysConfigService.updateSysConfig(sysConfig) <= 0) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_UPDATE_CODE_VALUE), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }


    /**
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 系统参数 刷新缓存
     * @data 2017-12-20
     */
    @RequestMapping(value = "configReload.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> reload() throws Exception {
        this.logger.info("------系统管理-系统参数-刷新缓存------");
        Map<String, Object> returnMap = new HashMap<String, Object>();
        // 调用缓存辅助类 重加载系统配置数据
        CacheUtil.initSysConfig();
        //前台缓存清理
        String webCleanUrl = Global.getValue("server_host") + "/system/config/reload.htm";
        String webResult = null;
        try {
            webResult = HttpUtil.getHttpResponse(webCleanUrl);
            logger.info("刷新api缓存结果:" + webResult);
        } catch (Exception e) {
            logger.info("刷新api缓存出错");
            logger.error(e.getMessage(), e);
        }
        if (StringUtil.isBlank(webResult)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MANAGE_SYS_MANAGE_CASHE_REFRESH_CODE_VALUE), HttpStatus.OK);
        }
        Map<String, Object> result = JsonUtil.parse(webResult, Map.class);
        String resultCode = StringUtil.isNull(result.get(Constant.RESPONSE_CODE));
        if (!(StringUtil.isNotBlank(resultCode)
                && StringUtil.isNull(Constant.SUCCEED_CODE_VALUE).equals(resultCode))) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_VALUE), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /******字典管理******/
    /**
     * @param current
     * @param pageSize
     * @param searchParams
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 字典管理 字典查询
     * @data 2017-12-21
     */
    @RequestMapping(value = "dictList.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> listDicts(@RequestParam(value = "current") Integer current,
                                                 @RequestParam(value = "pageSize") Integer pageSize,
                                                 @RequestParam(value = "searchParams", required = false) String searchParams) throws Exception {
        this.logger.info("------系统管理-字典管理-字典查询------");
        //参数解析
        Map<String, Object> searchMap = parseToMap(searchParams, true);
        //查询
        Page<SysDict> page = sysDictService.getDictPageList(current, pageSize, searchMap);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @param current
     * @param pageSize
     * @param id
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 字典管理 字项查询
     * @data 2017-12-21
     */
    @RequestMapping(value = "dictDetailFind.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> findDictDetail(@RequestParam(value = "current") Integer current,
                                                      @RequestParam(value = "pageSize") Integer pageSize,
                                                      @RequestParam(value = "id") Long id) throws Exception {
        this.logger.info("------系统管理-字典管理-字典字项查询------");
        //参数校验
        if (StringUtil.isEmpty(id)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        //获取详情
        Page<SysDictDetail> page = sysDictDetailService.getDictDetailList(current, pageSize, id);
        Map<String, Object> result = new HashMap<>();
        result.put(Constant.RESPONSE_DATA, page);
        result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    /**
     * @param sysDict
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 字典管理 新增/修改字典
     * @data 2017-12-21
     */
    @RequestMapping(value = "dictSaveOrUpdate.htm", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<ResultModel> saveOrUpdateDict(SysDict sysDict)
            throws Exception {
        this.logger.info("------系统管理-字典管理-新增/修改字典------");
        if (!sysDictService.addOrModify(sysDict)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_INSERT_CODE_VALUE), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * @param sysDictDetail
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 字典管理 新增/修改字典字项
     * @data 2017-12-21
     */
    @RequestMapping(value = "dictDetailSaveOrUpdate.htm", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<ResultModel> dictDetailSaveOrUpdate(SysDictDetail sysDictDetail) throws Exception {
        this.logger.info("------系统管理-字典管理-新增/修改字典字项------");
        if (!sysDictDetailService.addOrModify(sysDictDetail)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_INSERT_CODE_VALUE), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * @param id
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 字典管理 删除字典
     * @data 2017-12-21
     */
    @RequestMapping(value = "dictDelete.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> dictDelete(@RequestParam(value = "id") Long id) throws Exception {
        this.logger.info("------系统管理-字典管理-删除字典------");
        if (StringUtil.isEmpty(id)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        //若有字典字项则不允许删除
        if (sysDictDetailService.getItemCountMap(id) > 0) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.MANAGE_SYS_MANAGE_DICTDETIAL_EXIST_CODE_VALUE), HttpStatus.OK);
        } else {
            //删除字典
            if (!sysDictService.deleteDict(Long.valueOf(id))) {
                return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_DELETE_CODE_VALUE), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }

    /**
     * @param id
     * @return ResponseEntity<ResultModel>
     * @auther huangqin
     * @description 字典管理 删除字典字项
     * @data 2017-12-21
     */
    @RequestMapping(value = "dictDetailDelete.htm", method = {RequestMethod.POST})
    public ResponseEntity<ResultModel> dictDetailDelete(@RequestParam(value = "id") Long id) throws Exception {
        this.logger.info("------系统管理-字典管理-删除字典字项------");
        if (StringUtil.isEmpty(id)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_CODE_PARAM_INSUFFICIENT), HttpStatus.OK);
        }
        //删除字典字项
        if (!sysDictDetailService.deleteSysDictDetail(id)) {
            return new ResponseEntity<>(ResultModel.error(ManageExceptionEnum.FAIL_DELETE_CODE_VALUE), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }
}
