package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.UserIdnoMatchDictMapper;
import com.adpanshi.cashloan.manage.cl.model.UserIdnoMatchDict;
import com.adpanshi.cashloan.manage.cl.service.UserIdnoMatchDictService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 身份省份匹配serviceImpl
 * @date 2018/8/3 19:57
 */
@Service("UserIdnoMatchDictService")
public class UserIdnoMatchDictServiceImpl implements UserIdnoMatchDictService{
    private static final Logger logger = LoggerFactory.getLogger(UserIdnoMatchDictServiceImpl.class);
    @Resource
    private UserIdnoMatchDictMapper userIdnoMatchDictMapper;
    @Override
    public Page<UserIdnoMatchDict> list(Integer current, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(current,pageSize);
        Page<UserIdnoMatchDict> pages = (Page<UserIdnoMatchDict>) userIdnoMatchDictMapper.listSelective(params);
        return pages;
    }

    @Override
    public int save(UserIdnoMatchDict userIdnoMatchDict) {
        return userIdnoMatchDictMapper.insertSelective(userIdnoMatchDict);
    }

    @Override
    public int update(UserIdnoMatchDict userIdnoMatchDict) {
        return userIdnoMatchDictMapper.updateByPrimaryKeySelective(userIdnoMatchDict);
    }

    @Override
    public int updateState(Long id) {
        UserIdnoMatchDict idnoProvinceMatchDict = userIdnoMatchDictMapper.selectByPrimaryKey(id);
        UserIdnoMatchDict userIdnoMatchDict = new UserIdnoMatchDict();
        userIdnoMatchDict.setId(id);
        userIdnoMatchDict.setState(idnoProvinceMatchDict.getState()==1?0:1);
        return userIdnoMatchDictMapper.updateByPrimaryKeySelective(userIdnoMatchDict);
    }
}
