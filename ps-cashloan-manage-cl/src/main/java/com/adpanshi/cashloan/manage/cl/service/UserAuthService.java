package com.adpanshi.cashloan.manage.cl.service;


import com.adpanshi.cashloan.manage.cl.model.UserAuth;

/**
 * @author Vic Tang
 * @Description: 用户认证信息service
 * @date 2018/8/2 20:09
 */
public interface UserAuthService {
   /**
      *  获取用户认证信息
      * @param userId
      * @return UserAuth
      * @throws
      * @author Vic Tang
      * @date 2018/8/3 9:43
      * */
    UserAuth getUserAuthByUserId(Long userId);
}
