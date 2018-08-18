package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.UserContactsMatchDictMapper;
import com.adpanshi.cashloan.manage.cl.model.UserContactsMatchDict;
import com.adpanshi.cashloan.manage.cl.service.UserContactsMatchDictService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: TODO
 * @date 2018/8/3 19:55
 */
@Service("userContactsMatchDictService")
public class UserContactsMatchDictServiceImpl implements UserContactsMatchDictService{
    private static final Logger logger = LoggerFactory.getLogger(UserContactsMatchDictServiceImpl.class);
    @Resource
    private UserContactsMatchDictMapper userContactsMatchDictMapper;
    @Override
    public Page<UserContactsMatchDict> list(Integer current, Integer pageSize, Map<String, Object> searchMap) {
        PageHelper.startPage(current,pageSize);
        Page<UserContactsMatchDict> pages = (Page<UserContactsMatchDict>) userContactsMatchDictMapper.listSelective(searchMap);
        return pages;
    }

    @Override
    public int update(UserContactsMatchDict contactMatchDict) {
        return userContactsMatchDictMapper.updateByPrimaryKeySelective(contactMatchDict);
    }

    @Override
    public int save(UserContactsMatchDict contactMatchDict) {
        return userContactsMatchDictMapper.insertSelective(contactMatchDict);
    }

    @Override
    public int delById(Long id) {
        return userContactsMatchDictMapper.deleteByPrimaryKey(id);
    }
}
