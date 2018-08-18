package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.common.exception.ServiceException;
import com.adpanshi.cashloan.manage.cl.mapper.UserAppsMapper;
import com.adpanshi.cashloan.manage.cl.model.UserApps;
import com.adpanshi.cashloan.manage.cl.model.UserAppsExample;
import com.adpanshi.cashloan.manage.cl.service.UserAppsService;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.util.StringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Vic Tang
 * @Description: 用户应用信息serviceImpl
 * @date 2018/8/2 20:17
 */
@Service("userAppsService")
public class UserAppsServiceImpl implements UserAppsService{
    private static final Logger logger = LoggerFactory.getLogger(UserAppsServiceImpl.class);
    @Resource
    private UserAppsMapper userAppsMapper;
    @Override
    //TODO
    public Page<UserApps> getUserAppsPageList(Integer current, Integer pageSize, Long userId) throws ServiceException{
        if(StringUtil.isEmpty(userId)){
            throw new ServiceException("--------->userId不能为空}", Constant.FAIL_CODE_PARAM_INSUFFICIENT);
        }
        try {
            PageHelper.startPage(current, pageSize);
            UserAppsExample example = new UserAppsExample();
            example.createCriteria().andUserIdEqualTo(userId).andStateEqualTo(0);
            return(Page<UserApps>)userAppsMapper.selectByExample(example);

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new ServiceException(e.getMessage(),e,Constant.FAIL_CODE_PARAM_INSUFFICIENT);
        }
    }
}
