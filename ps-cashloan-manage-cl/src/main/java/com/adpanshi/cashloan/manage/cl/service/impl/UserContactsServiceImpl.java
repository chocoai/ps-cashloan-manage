package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.UserContactsMapper;
import com.adpanshi.cashloan.manage.cl.model.expand.UserContactsModel;
import com.adpanshi.cashloan.manage.cl.service.UserContactsService;
import com.adpanshi.cashloan.manage.core.common.util.ShardTableUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Page<UserContactsModel> listContactsNew(Long userId, String phone, Integer current, Integer pageSize) {
        List<UserContactsModel> list = new ArrayList<>();
        // 分表
        String tableName = ShardTableUtil.generateTableNameById("cl_user_contacts", userId, 30000);
        int countTable = userContactsMapper.countTable(tableName);
        if (countTable == 0) {
            return (Page<UserContactsModel>)list;
        }

        PageHelper.startPage(current, pageSize);
        Map<String,Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("phone",phone);
        list = userContactsMapper.listShardSelectiveNew(tableName, params);
        return (Page<UserContactsModel>)list;
    }
}
