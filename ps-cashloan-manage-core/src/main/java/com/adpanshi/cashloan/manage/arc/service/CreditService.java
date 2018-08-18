package com.adpanshi.cashloan.manage.arc.service;

import java.math.BigDecimal; /**
 * @author Vic Tang
 * @Description: 用户信用额度service
 * @date 2018/8/3 15:34
 */
public interface CreditService {
    /**
     *  放款后变更用户额度
     * @param userId
     * @param amount
     * @return int
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 15:39
     * */
    int modifyCreditAfterLoan(Long userId, BigDecimal amount);
}
