package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.expand.UserBaseInfoModel;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;

import java.util.List;

/**
 * @author Vic Tang
 * @Description: 用户基本信息service
 * @date 2018/8/2 20:01
 */
public interface UserBaseInfoService {
    /**
     *  根据用户id查询用户详情
     * @param userId
     * @return UserBaseInfoModel
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 11:25
     * */
    UserBaseInfoModel getBaseModelByUserId(Long userId);

    /**
     * 添加取现黑名单/取消.
     * @param userId
     * @param remarks
     * @param authUserRole
     * @return
     */
    int updateState(Long userId,String remarks,AuthUserRole authUserRole);

    /**
     *  放款用户详情
     * @param
     * @throws
     * @author Vic Tang
     * @date 2018/8/2 17:43
     * */
    List<UserBaseInfoModel> findLoanUserInfo();
}
