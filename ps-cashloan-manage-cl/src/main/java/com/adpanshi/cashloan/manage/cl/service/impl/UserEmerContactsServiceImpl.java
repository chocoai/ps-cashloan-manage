package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.UserEmerContactsMapper;
import com.adpanshi.cashloan.manage.cl.model.UserEmerContacts;
import com.adpanshi.cashloan.manage.cl.model.UserEmerContactsExample;
import com.adpanshi.cashloan.manage.cl.service.UserEmerContactsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Vic Tang
 * @Description: 紧急联系人serviceImpl
 * @date 2018/8/2 20:15
 */
@Service("userEmerContactsService")
public class UserEmerContactsServiceImpl implements UserEmerContactsService{
    private static final Logger logger = LoggerFactory.getLogger(UserEmerContactsServiceImpl.class);
    @Resource
    private UserEmerContactsMapper userEmerContactsMapper;
    @Override
    public List<UserEmerContacts> getUserEmerContactsByUserId(Long userId) {
        UserEmerContactsExample example = new UserEmerContactsExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return userEmerContactsMapper.selectByExample(example);
    }
}
