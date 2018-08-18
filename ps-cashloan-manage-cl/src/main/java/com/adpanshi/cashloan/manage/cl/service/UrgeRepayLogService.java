package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.UrgeRepayLog;
import com.adpanshi.cashloan.manage.cl.model.UrgeRepayOrder;
import com.github.pagehelper.Page;

import java.util.Map; /**
 * @author Vic Tang
 * @Description: 催收反馈service
 * @date 2018/8/1 20:30
 */
public interface UrgeRepayLogService {
    /**
     * 根据条件查询催款记录信息
     *
     * @param dueId
     * @return
     */
    Page<UrgeRepayLog> getLogByParam(Long dueId, Integer current, Integer pageSize);
    /**
     *  保存催款记录
     * @param urgeRepayLog
     * @param order
     * @return void
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 11:02
     * */
    void saveOrderInfo(UrgeRepayLog urgeRepayLog, UrgeRepayOrder order);
}
