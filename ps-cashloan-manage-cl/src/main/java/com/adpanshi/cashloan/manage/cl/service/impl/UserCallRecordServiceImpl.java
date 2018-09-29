package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.UserCallRecordMapper;
import com.adpanshi.cashloan.manage.cl.model.UserCallRecord;
import com.adpanshi.cashloan.manage.cl.service.UserCallRecordService;
import com.adpanshi.cashloan.manage.core.common.util.ShardTableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Vic Tang
 * @Description: 用户通话记录serviceImpl
 * @date 2018/8/30 10:37
 */
@Service("UserCallRecordService")
public class UserCallRecordServiceImpl implements UserCallRecordService{
    private static final Logger logger = LoggerFactory.getLogger(UserCallRecordService.class);
    @Resource
    private UserCallRecordMapper userCallRecordMapper;
    @Override
    public UserCallRecord getByUserId(Long userId) {
        // 分表
        String tableName = ShardTableUtil.generateTableNameById("cl_user_call_record", userId, 30000);
        int countTable = userCallRecordMapper.countTable(tableName);
        if (countTable == 0) {
            return null;
        }
        return userCallRecordMapper.getByUserId(tableName,userId);
    }
}
