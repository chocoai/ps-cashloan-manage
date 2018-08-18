package com.adpanshi.cashloan.manage.arc.service;

import com.adpanshi.cashloan.manage.arc.model.SysConfig;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map; /**
 * @author Vic Tang
 * @Description: 系统参数service
 * @date 2018/8/3 21:21
 */
public interface SysConfigService {
    /**
     * 系统参数表表,查询数据
     * @param currentPage
     * @param pageSize
     * @param map
     * @return  返回页面map
     * @throws Exception
     */
    Page<SysConfig> getSysConfigPageList (int currentPage,int pageSize,Map<String, Object> map);

    /**
     * 系统参数表表,修改数据
     * @param sysConfig 系统参数表类
     * @return           返回页面map
     * @throws Exception
     */
    int updateSysConfig(SysConfig sysConfig);
    /**
     * 根据code查询
     * @param code
     * @return SysConfig
     * */
    SysConfig selectByCode(String code);
    /**
     * 系统参数表表,插入数据
     * @param sysConfig 系统参数表类
     * @return           返回页面map
     * @throws Exception
     */
    int insertSysConfig(SysConfig sysConfig);
    /**
     * 查询所有配置
     * @return ist<SysConfig>
     */
    List<SysConfig> findAll();
}
