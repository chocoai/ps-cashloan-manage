package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.UserMapper;
import com.adpanshi.cashloan.manage.cl.model.User;
import com.adpanshi.cashloan.manage.cl.model.expand.UserBaseInfoModel;
import com.adpanshi.cashloan.manage.cl.pojo.AuthUserModel;
import com.adpanshi.cashloan.manage.cl.service.UserService;
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
 * @Description: app用户serviceImpl
 * @date 2018/8/3 11:15
 */
@Service("userService")
public class UserServiceImpl implements UserService{
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource
    private UserMapper userMapper;

    @Override
    public User getById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<UserBaseInfoModel> listUser(Map<String, Object> params, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<UserBaseInfoModel> list = userMapper.listModel(params);
        return (Page<UserBaseInfoModel>) list;
    }

    @Override
    public Page<UserBaseInfoModel> listReUser(Map<String, Object> params, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<UserBaseInfoModel> list = userMapper.reListModelNew(params);
        return (Page<UserBaseInfoModel>) list;
    }

    @Override
    public Page<AuthUserModel> listAuthUser(Map<String, Object> params, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<AuthUserModel> list = userMapper.listAuthUserModel(params);
        return (Page<AuthUserModel>) list;
    }
}
