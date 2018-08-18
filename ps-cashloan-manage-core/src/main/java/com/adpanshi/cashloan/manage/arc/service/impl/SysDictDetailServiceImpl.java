package com.adpanshi.cashloan.manage.arc.service.impl;

import com.adpanshi.cashloan.manage.arc.mapper.SysDictDetailMapper;
import com.adpanshi.cashloan.manage.arc.model.SysDictDetail;
import com.adpanshi.cashloan.manage.arc.model.SysDictDetailExample;
import com.adpanshi.cashloan.manage.arc.service.SysDictDetailService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 系统数据字典详情serviceImpl
 * @date 2018/8/2 20:30
 */
@Service("sysDictDetailService")
public class SysDictDetailServiceImpl implements SysDictDetailService{
    public static final Logger logger = LoggerFactory.getLogger(SysDictDetailServiceImpl.class);
    @Resource
    private SysDictDetailMapper sysDictDetailMapper;

    @Override
    public Page<SysDictDetail> getDictDetailList(Integer current, Integer pageSize, Long id) {
        PageHelper.startPage(current, pageSize);
        SysDictDetailExample example = new SysDictDetailExample();
        example.createCriteria().andParentIdEqualTo(id);
        return (Page<SysDictDetail>) sysDictDetailMapper.selectByExample(example);
    }

    @Override
    public List<SysDictDetail> listByTypeCode(Map<String, Object> data) {
        return sysDictDetailMapper.listByTypeCode(data);
    }

    @Override
    public boolean addOrModify(SysDictDetail sysDictDetail) {
        int count = 0;
        if (null != sysDictDetail.getId() && sysDictDetail.getId().longValue() > 0L) {
            count = sysDictDetailMapper.updateByPrimaryKeySelective(sysDictDetail);
        } else {
            count = sysDictDetailMapper.insertSelective(sysDictDetail);
        }
        return count > 0;
    }

    @Override
    public Integer getItemCountMap(Long id) {
        SysDictDetailExample example = new SysDictDetailExample();
        example.createCriteria().andParentIdEqualTo(id);
        return sysDictDetailMapper.selectByExample(example).size();
    }

    @Override
    public boolean deleteSysDictDetail(Long id) {
        return sysDictDetailMapper.deleteByPrimaryKey(id) > 0;
    }
}
