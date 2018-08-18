package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.QuartzLogMapper;
import com.adpanshi.cashloan.manage.cl.model.QuartzLog;
import com.adpanshi.cashloan.manage.cl.model.expand.QuartzLogModel;
import com.adpanshi.cashloan.manage.cl.service.QuartzLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 定时任务日志serviceImpl
 * @date 2018/8/3 23:53
 */
@Service("quartzLogService")
public class QuartzLogServiceImpl implements QuartzLogService {
    private static final Logger logger = LoggerFactory.getLogger(QuartzLogServiceImpl.class);
    @Resource
    private QuartzLogMapper quartzLogMapper;
    @Override
    public Page<QuartzLogModel> page(Map<String, Object> searchMap, Integer current, Integer pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<QuartzLogModel>) quartzLogMapper.page(searchMap);
    }

    @Override
    public void save(QuartzLog ql) {
        quartzLogMapper.insertSelective(ql);
    }
}
