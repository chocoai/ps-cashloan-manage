package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.UserBaseInfoMapper;
import com.adpanshi.cashloan.manage.cl.model.UserBaseInfo;
import com.adpanshi.cashloan.manage.cl.model.expand.UserBaseInfoModel;
import com.adpanshi.cashloan.manage.cl.service.UserBaseInfoService;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Vic Tang
 * @Description: 用户基本信息serviceImpl
 * @date 2018/8/2 20:01
 */
@Service("userBaseInfoService")
public class UserBaseInfoServiceImpl implements UserBaseInfoService{
    private static final Logger logger = LoggerFactory.getLogger(UserBaseInfoServiceImpl.class);
    @Resource
    private UserBaseInfoMapper userBaseInfoMapper;
    @Override
    public UserBaseInfoModel getBaseModelByUserId(Long userId) {
        return userBaseInfoMapper.getBaseModelByUserId(userId);
    }

    @Override
    public int updateState(Long userId, String remarks, AuthUserRole authUserRole) {
        int i = 0;
        UserBaseInfo base = userBaseInfoMapper.getBaseModelByUserId(userId);
        if (base != null) {
            String state = StringUtils.isEmpty(base.getState())?"20":("10".equals(base.getState())?"20":"10");
            StringBuffer sb = new StringBuffer();
            sb.append(base.getBlackReason()).append("|").append(new Date()).append("|").append(base.getState()).append("-").append(state).append("|")
                    .append(authUserRole.getUserId()).append("|").append(authUserRole.getUserName()).append("|").append(remarks).append("|");
            UserBaseInfo baseInfo = new UserBaseInfo();
            baseInfo.setId(base.getId());
            baseInfo.setState(state);
            baseInfo.setBlackReason(sb.toString());
            i = userBaseInfoMapper.updateByPrimaryKeySelective(baseInfo);
        }
        return i;
    }

    @Override
    public List<UserBaseInfoModel> findLoanUserInfo() {
        return userBaseInfoMapper.findLoanUserInfo();
    }
}
