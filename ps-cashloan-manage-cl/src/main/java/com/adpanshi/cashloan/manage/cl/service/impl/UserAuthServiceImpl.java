package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.UserAuthMapper;
import com.adpanshi.cashloan.manage.cl.model.UserAuth;
import com.adpanshi.cashloan.manage.cl.model.UserAuthExample;
import com.adpanshi.cashloan.manage.cl.service.UserAuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Vic Tang
 * @Description: 用户认证信息serviceImpl
 * @date 2018/8/2 20:13
 */
@Service("userAuthService")
public class UserAuthServiceImpl implements UserAuthService {
    private static final Logger logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);
    @Resource
    private UserAuthMapper userAuthMapper;
    @Override
    public UserAuth getUserAuthByUserId(Long userId) {
        UserAuthExample example = new UserAuthExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UserAuth> infos = userAuthMapper.selectByExample(example);
        if(infos.size() > 0){
            return infos.get(0);
        } else {
            return null;
        }
    }
}
