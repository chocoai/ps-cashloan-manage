package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.QuartzInfo;
import com.adpanshi.cashloan.manage.cl.model.expand.QuartzInfoModel;
import com.github.pagehelper.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 定时任务service
 * @date 2018/8/3 23:53
 */
public interface QuartzInfoService {
    /**
     * 查询所有任务
     *
     * @param result
     * @return
     */
    List<QuartzInfo> list(Map<String, Object> result);
    /**
     * 定时任务分页查询
     *
     * @param searchMap
     * @param current
     * @param pageSize
     * @return
     */
    Page<QuartzInfoModel> page(Map<String, Object> searchMap, int current, int pageSize);

    QuartzInfo getById(Long id);

    boolean update(QuartzInfo info);

    QuartzInfo findSelective(String code);

    boolean save(QuartzInfo quartzInfo);
}
