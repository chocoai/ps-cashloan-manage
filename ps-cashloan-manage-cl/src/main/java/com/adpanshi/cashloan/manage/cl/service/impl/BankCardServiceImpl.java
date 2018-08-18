package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.BankCardMapper;
import com.adpanshi.cashloan.manage.cl.model.BankCard;
import com.adpanshi.cashloan.manage.cl.model.BankCardExample;
import com.adpanshi.cashloan.manage.cl.service.BankCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Vic Tang
 * @Description: 银行卡信息serviceImpl
 * @date 2018/8/2 20:14
 */
@Service("bankCardService")
public class BankCardServiceImpl implements BankCardService{
    private static final Logger logger = LoggerFactory.getLogger(BankCardServiceImpl.class);
    @Resource
    private BankCardMapper bankCardMapper;
    @Override
    public BankCard getBankCardByUserId(Long userId) {
        BankCardExample example = new BankCardExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<BankCard> infos = bankCardMapper.selectByExample(example);
        if(infos.size() > 0){
            return infos.get(0);
        } else {
            return null;
        }
//        return bankCardMapper.selectByExample(example).get(0);
    }
}
