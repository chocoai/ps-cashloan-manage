package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.UserContacts;
import com.adpanshi.cashloan.manage.cl.model.expand.UserContactsModel;
import com.github.pagehelper.Page;

/**
 * @author Vic Tang
 * @Description: 用户通讯录信息service
 * @date 2018/8/2 20:11
 */
public interface UserContactsService {
    /**
     *  获取用户通讯录信息
     * @param userId
     * @param phone
     * @param current
     * @param pageSize
     * @return Page<UserContacts>
     * @throws
     * @author Vic Tang
     * @date 2018/8/6 11:11
     * */
    Page<UserContactsModel> listContactsNew(Long userId,String phone,Integer current, Integer pageSize);
}
