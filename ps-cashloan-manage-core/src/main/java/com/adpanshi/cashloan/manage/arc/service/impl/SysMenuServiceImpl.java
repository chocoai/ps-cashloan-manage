package com.adpanshi.cashloan.manage.arc.service.impl;



import com.adpanshi.cashloan.manage.arc.mapper.SysMenuMapper;
import com.adpanshi.cashloan.manage.arc.mapper.SysRoleMenuMapper;
import com.adpanshi.cashloan.manage.arc.model.SysRoleMenuExample;
import com.adpanshi.cashloan.manage.arc.service.SysMenuService;
import com.adpanshi.cashloan.manage.core.common.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 系统菜单serviceImpl
 * @date 2018/8/1 15:40
 */
@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService{
    public static final Logger logger = LoggerFactory.getLogger(SysMenuServiceImpl.class);
    @Resource
    private SysMenuMapper sysMenuMapper;
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<Map<String, Object>> fetchRoleMenus(String sysType, Long roleId) {
        Map<String,Object> searchParam=new HashMap<>();
        searchParam.put("roleId",roleId);
        searchParam.put("isMenu",1);
        //根据id查询菜单
        List<Map<String, Object>> menuList = sysMenuMapper.getMenuByRoleId(searchParam);
        //将菜单按照（父节点ID）value=（子节点的父节点ID）parentId排列成树
        menuList = ListUtil.list2Tree(menuList, "value", "parentId");
        searchParam.put("isMenu",0);
        //获取角色所有的权限
        List<Map<String, Object>> btnPerms = sysMenuMapper.getMenuByRoleId(searchParam);
        for (Map<String, Object> menuMap : menuList) {
            List<Map> children = (List<Map>) menuMap.get("children");
            if(null == children || children.size() <= 0 ){
                continue;
            }
            for (Map mp : children) {
                //根据 菜单menuId 查询菜单下的按钮权限
                long menuId = Long.parseLong(mp.get("value").toString());
                if (menuId <= 0) {
                    continue;
                }
                Map<String,ArrayList<Map<String, Object>>> btnMap=new HashMap<>();
                for (Map<String, Object> btnPerm : btnPerms) {
                    long roleMenuId = Long.parseLong(String.valueOf(
                            null == btnPerm.get("parentId") ? "0" : btnPerm.get("parentId")));
                    if (roleMenuId <= 0) {
                        continue;
                    }
                    if (roleMenuId == menuId) {
                        ArrayList<Map<String, Object>> btn=btnMap.get(String.valueOf(
                                null == btnPerm.get("menuType") ?"0" : btnPerm.get("menuType")));
                        if(null != btn){
                            btn.add(btnPerm);
                        }else{
                            btn = new ArrayList<>();
                            btn.add(btnPerm);
                        }
                        btnMap.put(String.valueOf(btnPerm.get("menuType")),btn);
                    }
                }
                mp.put("BtnPermList", btnMap);
            }
        }
        return menuList;
    }

    @Override
    public List<Map<String, Object>> fetchRoleMenuHas(Long roleId) {
        return sysMenuMapper.fetchRoleMenuHas(roleId);
    }

    @Override
    public List<Map<String, Object>> fetchRolebtnHas(Long roleId) {
        return sysMenuMapper.fetchRolebtnHas(roleId);
    }

    @Override
    public boolean updateRoleMenuHas(Long roleId, Long[] menus) {
        SysRoleMenuExample example = new SysRoleMenuExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        //删除原有的关系
        sysRoleMenuMapper.deleteByExample(example);
        //批量新增
        return sysRoleMenuMapper.saveBantch(roleId,menus) > 0;
    }
}
