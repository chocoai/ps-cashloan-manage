package com.adpanshi.cashloan.manage.cl.service;


import com.adpanshi.cashloan.manage.cl.model.UserCallRecord;

/**
 * 用户通话记录Service
 * 
 * @author Vic Tang
 * @version 1.0.0
 * @date 2018-08-15 15:07:31

 *
 *
 */
public interface UserCallRecordService {
    /**
     *  获取用户最新的通话记录信息
     * @param userId
     * @return UserCallRecord
     * @throws
     * @author Vic Tang
     * @date 2018/8/15 15:50
     * */
    UserCallRecord getByUserId(Long userId);
}
