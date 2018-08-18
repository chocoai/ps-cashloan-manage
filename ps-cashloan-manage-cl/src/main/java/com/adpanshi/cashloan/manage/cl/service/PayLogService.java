package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.PayLog;
import com.adpanshi.cashloan.manage.cl.model.expand.PayLogModel;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map; /**
 * @author Vic Tang
 * @Description: 支付记录service
 * @date 2018/8/3 11:49
 */
public interface PayLogService {
    /**
     *  分页查询支付记录
     * @param current
     * @param pageSize
     * @param searchMap
     * @return  Page<PayLogModel>
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 14:19
     * */
    Page<PayLogModel> page(Integer current, Integer pageSize, Map<String, Object> searchMap);
    /**
     * <p>根据orderNo、borrowId查找最近(创建时间:create_time倒序)的一条支付记录</p>
     * @param orderNo  订单号(必填)
     * @param borrowId 借款id(非必填)
     * @return PayLog
     * */
    PayLog findPayLogByLastOrderNoWithBorrowId(String orderNo, String borrowId);
    /**
     *  更新订单状态
     * @param record
     * @return void
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 15:17
     * */
    boolean updateSelective(PayLog record);
    /**
     * 导出查询
     * @param searchMap
     * @return
     */
    @SuppressWarnings("rawtypes")
    List listPayLog(Map<String, Object> searchMap);
}
