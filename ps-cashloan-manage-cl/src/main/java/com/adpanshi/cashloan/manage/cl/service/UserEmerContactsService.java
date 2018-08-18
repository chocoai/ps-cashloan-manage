package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.UserEmerContacts;

import java.util.List;

/**
 * @author Vic Tang
 * @Description: 紧急联系人service
 * @date 2018/8/2 20:10
 */
public interface UserEmerContactsService {
    /**
     *  获取用户紧急联系人信息
     * @param userId
     * @return List<UserEmerContacts>
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 9:39
     * */
    List<UserEmerContacts> getUserEmerContactsByUserId(Long userId);
}
