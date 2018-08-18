package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.Notices;
import com.github.pagehelper.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map; /**
 * @author Vic Tang
 * @Description: 消息service
 * @date 2018/8/4 0:15
 */
public interface NoticesService {
    /**
     * 获取消息列表
     * @param params
     * @param currentPage
     * @param pageSize
     * @return
     */
    Page<Notices> queryNoticesList(Map<String, Object> params, int currentPage, int pageSize);

    int insert(Notices notices);

    int updateById(Notices notices);

    Notices findByPrimary(Long id);

    /**
     * 放款
     * @method: payment
     * @param userId
     * @param borrowMainId
     * @param amount
     * @return: void
     * @throws
     * @Author: Mr.Wange
     * @Date: 2018/7/25 16:13
     */
    void payment(Long userId, Long borrowMainId, BigDecimal amount);

    /**
     * 主动还款消息
     * @method: activePayment
     * @param userId
     * @param borrowMainId
     * @param settleTime
     * @param repayAmount
     * @return: void
     * @throws
     * @Author: Mr.Wange
     * @Date: 2018/7/25 14:44
     */
    void activePayment(long userId, long borrowMainId, Date settleTime, BigDecimal repayAmount);

    /**
     * 审核未通过
     * @method: refuse
     * @param userId
     * @param day
     * @return: void
     * @throws
     * @Author: Mr.Wange
     * @Date: 2018/7/25 15:59
     */
    void refuse(long userId, int day);

    /**
     * 逾期
     * @method: overdue
     * @param borrowId
     * @param userId
     * @return: void
     * @throws
     * @Author: Mr.Wange
     * @Date: 2018/7/25 16:25
     */
    void overdue(long borrowId,long userId);
    /**
     * 还款提醒
     * @method: repayBefore
     * @param userId
     * @param borrowId
     * @return: void
     * @throws
     * @Author: Mr.Wange
     * @Date: 2018/7/25 16:29
     */
    void repayBefore(long userId, long borrowId);
}
