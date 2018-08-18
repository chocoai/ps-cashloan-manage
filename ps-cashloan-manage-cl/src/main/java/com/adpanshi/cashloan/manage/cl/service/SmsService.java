package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.Sms;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map; /**
 * @author Vic Tang
 * @Description: 短信service
 * @date 2018/8/6 23:02
 */
public interface SmsService {
    /**
     * 立即还款成功
     *
     * @param userId 用户id
     * @param borrowMainId 主借款订单id
     * @param repayAmount 还款金额
     * @param repayOrderNo 还款单号（计算还款期数）
     * @return
     */
    int activePayment(long userId, long borrowMainId, Date settleTime, BigDecimal repayAmount, String repayOrderNo);
    /**
     * 放款成功短信发送
     * */
    int payment(Long userId, Long borrowMainId, Date date, BigDecimal amount, String orderNo);
    /**
     * 逾期发送通知短信
     * @param borrowId
     */
    int overdue(long borrowId,long userId);

    /**
     * 还款前通知
     */
    int repayBefore(long userId,long borrowId);

    /**
     * 审核不通过通知
     */
    int refuse(long userId, int day, String orderNo);

    /**
     * 获取时间段内的需重发短信列表
     * */
    List<Sms> getResendSmsList(Map<String, Object> map);

    void updateByIds(Map<String, Object> clSmsInfo);

    void updateByMsgids(Map<String, Object> tyhSmsInfo);

    /**
     * 根据手机号码、短信验证码类型查询今日可获取次数，防短信轰炸
     * @param phone
     * @param email
     * @param type
     * @return
     */
    int countDayTime(String phone, String email, String type);


    /**
     * 发送短信
     * @return
     */
    int sendMsg(Sms sms);
}
