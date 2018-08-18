package com.adpanshi.cashloan.manage.arc.service;

import com.adpanshi.cashloan.manage.arc.model.SysDict;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 系统字典service
 * @date 2018/8/2 20:23
 */
public interface SysDictService {
    /**
     *  根据code获取系统字典
     * @param code
     * @return SysDict
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 10:02
     * */
    SysDict findByTypeCode(String code);

    /**
     * @param type
     * @return RList<Map<String, Object>>
     * @auther huangqin
     * @description 获取系统参数类型数据字典
     * @data 2017-12-20
     */
    List<Map<String, Object>> getDictsCache(String type);
    /**
     * @param current
     * @param pageSize
     * @param searchMap
     * @return Page<SysDict>
     * @auther huangqin
     * @description 字典管理 字典查询
     * @data 2017-12-21
     */
    Page<SysDict> getDictPageList(Integer current, Integer pageSize, Map<String, Object> searchMap);

    /**
     * @param sysDict
     * @return boolean
     * @auther huangqin
     * @description 字典管理 字典修改/查询
     * @data 2017-12-21
     */
    boolean addOrModify(SysDict sysDict);

    /**
     * @param id
     * @return boolean
     * @auther huangqin
     * @description 字典管理 字典删除
     * @data 2017-12-21
     */
    boolean deleteDict(Long id);
}
