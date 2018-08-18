package com.adpanshi.cashloan.manage.arc.service.impl;

import com.adpanshi.cashloan.manage.arc.mapper.SysDictMapper;
import com.adpanshi.cashloan.manage.arc.model.SysDict;
import com.adpanshi.cashloan.manage.arc.model.SysDictExample;
import com.adpanshi.cashloan.manage.arc.service.SysDictService;
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
 * @Description: 系统字典serviceImpl
 * @date 2018/8/2 20:28
 */
@Service("sysDictService")
public class SysDictServiceImpl implements SysDictService{
    public static final Logger logger = LoggerFactory.getLogger(SysDictServiceImpl.class);
    @Resource
    private SysDictMapper sysDictMapper;
    @Override
    public SysDict findByTypeCode(String code) {
        SysDictExample example = new SysDictExample();
        example.createCriteria().andTypeCodeEqualTo(code);
        List<SysDict> infos = sysDictMapper.selectByExample(example);
        if(infos.size() > 0){
            return infos.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getDictsCache(String type) {
        return sysDictMapper.getDictsCache(type);
    }

    @Override
    public Page<SysDict> getDictPageList(Integer current, Integer pageSize, Map<String, Object> searchMap) {
        PageHelper.startPage(current, pageSize);
        return (Page<SysDict>) sysDictMapper.listSelective(searchMap);
    }

    @Override
    public boolean addOrModify(SysDict sysDict) {
        int num = 0;
        if (null != sysDict.getId() && sysDict.getId().longValue() > 0L) {
            num = sysDictMapper.updateByPrimaryKeySelective(sysDict);
        } else {
            num = sysDictMapper.insertSelective(sysDict);
        }
        return num > 0;
    }

    @Override
    public boolean deleteDict(Long id) {
        return sysDictMapper.deleteByPrimaryKey(id) > 0;
    }
}
