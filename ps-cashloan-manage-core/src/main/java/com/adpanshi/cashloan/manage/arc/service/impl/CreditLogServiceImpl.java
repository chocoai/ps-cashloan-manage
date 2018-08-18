package com.adpanshi.cashloan.manage.arc.service.impl;

import com.adpanshi.cashloan.manage.arc.service.CreditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Vic Tang
 * @Description: 用户额度日志serviceImpl
 * @date 2018/8/3 15:37
 */
@Service("creditLogService")
public class CreditLogServiceImpl implements CreditLogService{
    public static final Logger logger = LoggerFactory.getLogger(CreditLogServiceImpl.class);
}
