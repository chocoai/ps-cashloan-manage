package com.adpanshi.cashloan.manage.arc.service;

import com.adpanshi.cashloan.manage.arc.model.SysDictDetail;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 系统字典详情
 * @date 2018/8/2 20:23
 */
public interface SysDictDetailService {
    /**
     *  获取用户字典详情
     * @param current
     * @param pageSize
     * @param id
     * @return Page<SysDictDetail>
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 10:40
     * */
    Page<SysDictDetail> getDictDetailList(Integer current, Integer pageSize, Long id);

    /**
     * 根据code查询字典
     * @param data
     * @return List<SysDictDetail>
     */
    List<SysDictDetail> listByTypeCode(Map<String, Object> data);
    /**
     * 新增或者修改字典详情信息
     *
     * @param dictDetail
     * @return boolean
     */
    boolean addOrModify(SysDictDetail dictDetail);

    /**
     * 详情个数查询
     *
     * @param id
     * @return Integer
     */
    Integer getItemCountMap(Long id);
    /**
     * 字典详情信息表删除
     *
     * @param id 主键ID
     * @return boolean
     */
    boolean deleteSysDictDetail(Long id);
}
