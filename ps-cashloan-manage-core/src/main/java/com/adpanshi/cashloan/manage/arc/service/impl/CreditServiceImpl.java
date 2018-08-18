package com.adpanshi.cashloan.manage.arc.service.impl;

import com.adpanshi.cashloan.common.exception.BussinessException;
import com.adpanshi.cashloan.manage.arc.mapper.CreditMapper;
import com.adpanshi.cashloan.manage.arc.model.Credit;
import com.adpanshi.cashloan.manage.arc.model.CreditExample;
import com.adpanshi.cashloan.manage.arc.service.CreditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Vic Tang
 * @Description: 用户信用额度serviceImpl
 * @date 2018/8/3 15:36
 */
@Service("CreditService")
public class CreditServiceImpl implements CreditService{
    public static final Logger logger = LoggerFactory.getLogger(CreditServiceImpl.class);
    @Resource
    private CreditMapper creditMapper;
    @Override
    public int modifyCreditAfterLoan(Long userId, BigDecimal amount) {
        CreditExample example =  new CreditExample();
        example.createCriteria().andConsumerNoEqualTo(userId.toString());
        List<Credit> infos = creditMapper.selectByExample(example);
        if(infos.size() > 0){
            Credit credit = infos.get(0);
            BigDecimal diff = credit.getUnuse().subtract(amount);
            if(diff.compareTo(BigDecimal.ZERO) != -1){
                BigDecimal used = credit.getUsed().add(amount);
                Credit record = new Credit();
                record.setId(credit.getId());
                record.setUnuse(diff);
                record.setUsed(used);
                return creditMapper.updateByPrimaryKeySelective(record);
            }else{
                throw new BussinessException("【放款】更新额度失败，不能出现负值,userId:" + userId);
            }
        } else {
            throw new BussinessException("【放款】更新额度失败，未找到用户额度信息，userId:" + userId);
        }
    }
}
