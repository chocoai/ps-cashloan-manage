package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.UserContactsMapper;
import com.adpanshi.cashloan.manage.cl.model.UserContacts;
import com.adpanshi.cashloan.manage.cl.model.UserContactsExample;
import com.adpanshi.cashloan.manage.cl.service.UserContactsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Vic Tang
 * @Description: 用户通讯录信息serviceImpl
 * @date 2018/8/2 20:18
 */
@Service("UserContactsService")
public class UserContactsServiceImpl implements UserContactsService{
    private static final Logger logger = LoggerFactory.getLogger(UserContactsServiceImpl.class);
    @Resource
    private UserContactsMapper userContactsMapper;
    @Override
    //TODO
    public Page<UserContacts> listContactsNew(Long userId, Integer current, Integer pageSize) {
        PageHelper.startPage(current, pageSize);
        UserContactsExample example = new UserContactsExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UserContacts> list = userContactsMapper.selectByExample(example);
        return (Page<UserContacts>)list;
    }
}
