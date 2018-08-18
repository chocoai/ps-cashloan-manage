package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.BankCard;

/**
 * @author Vic Tang
 * @Description: 银行卡信息service
 * @date 2018/8/2 20:10
 */
public interface BankCardService {
    /**
     *  获取用户银行卡信息
     * @param userId
     * @return BankCard
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 9:34
     * */
    BankCard getBankCardByUserId(Long userId);
}
