package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.OverduePhoneMapper;
import com.adpanshi.cashloan.manage.cl.model.OverduePhone;
import com.adpanshi.cashloan.manage.cl.service.OverduePhoneService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 催收号serviceImpl
 * @date 2018/8/3 19:56
 */
@Service("OverduePhoneService")
public class OverduePhoneServiceImpl implements OverduePhoneService{
    private static final Logger logger = LoggerFactory.getLogger(OverduePhoneServiceImpl.class);
    @Resource
    private OverduePhoneMapper overduePhoneMapper;
    @Override
    public Page<OverduePhone> list(int current, int pageSize, Map<String, Object> searchMap) {
        PageHelper.startPage(current,pageSize);
        return  (Page<OverduePhone>) overduePhoneMapper.listByCondition(searchMap);
    }

    @Override
    public int update(OverduePhone overduePhone) {
        return overduePhoneMapper.updateByPrimaryKeySelective(overduePhone);
    }

    @Override
    public int save(OverduePhone overduePhone) {
        return overduePhoneMapper.insertSelective(overduePhone);
    }

    @Override
    public int deleteByIds(List<Long> ids) {
        return overduePhoneMapper.deleteByIds(ids);
    }
}
