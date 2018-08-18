package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.QuartzLog;
import com.adpanshi.cashloan.manage.cl.model.expand.QuartzLogModel;
import com.github.pagehelper.Page;

import java.util.Map; /**
 * @author Vic Tang
 * @Description: 定时任务日志service
 * @date 2018/8/3 23:53
 */
public interface QuartzLogService {
    /**
     * 执行记录查询
     */
    Page<QuartzLogModel> page(Map<String, Object> searchMap, Integer current, Integer pageSize);

    void save(QuartzLog ql);
}
