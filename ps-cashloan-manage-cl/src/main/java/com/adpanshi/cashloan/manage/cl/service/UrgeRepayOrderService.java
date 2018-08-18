package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.common.exception.ManageException;
import com.adpanshi.cashloan.manage.cl.model.UrgeRepayOrder;
import com.adpanshi.cashloan.manage.cl.model.expand.UrgeRepayCountModel;
import com.adpanshi.cashloan.manage.cl.model.expand.UrgeRepayOrderModel;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map; /**
 * @author Vic Tang
 * @Description: 催收订单service
 * @date 2018/8/1 20:29
 */
public interface UrgeRepayOrderService {
    /**
     *  催收订单查询
     * @param params
     * @param current
     * @param pageSize
     * @return Page<UrgeRepayOrderModel>
     * @throws
     * @author Vic Tang
     * @date 2018/8/1 21:00
     * */
    Page<UrgeRepayOrderModel> list(Map<String, Object> params, Integer current, Integer pageSize);

    /**
     * 催款记录详细信息
     *
     * @param params
     * @param current
     * @param pageSize
     * @return
     */
    Page<UrgeRepayOrderModel> listModel(Map<String, Object> params, int current,
                                        int pageSize);
    /**
     * 催回率统计
     *
     * @param params
     * @param current
     * @param pageSize
     * @return
     */
    Page<UrgeRepayCountModel> urgeCount(Map<String, Object> params, int current,
                                        int pageSize);

    /**
     * 催收人员统计列表
     *
     * @param params
     * @param current
     * @param pageSize
     * @return
     */
    Page<UrgeRepayCountModel> memberCount(Map<String, Object> params, int current,
                                          int pageSize);

    /**
     * 催收订单统计
     *
     * @param params
     * @param current
     * @param pageSize
     * @return
     */
    Page<UrgeRepayCountModel> orderCount(Map<String, Object> params, int current,
                                         int pageSize);
    /**
     * 催收员每日统计
     *
     * @param params
     * @param current
     * @param pageSize
     * @return
     */
    Page<UrgeRepayCountModel> memberDayCount(Map<String, Object> params,
                                             int current, int pageSize);

    /**
     * 催款记录信息
     *
     * @param params
     * @param current
     * @param pageSize
     * @return
     */
    Page<UrgeRepayOrderModel> listManage(Map<String, Object> params, int current,
                                           int pageSize);

    /**
     * 修改逾期信息
     *
     * @param id
     * @param state
     */
    int updateLate(Long id,String state);

    /**
     * 根据借款id添加催收信息
     *
     * @param id
     * @return
     */
    void saveOrder(Long id) throws ManageException;
    /**
     * 催款订单是否为催收管理员订单
     *
     * @param
     * @return
     */
    boolean isCollector(Long id);

    /**
     * 指派催收人员
     *
     * @param params
     * @return
     */
    int orderAllotUser(Map<String, Object> params);

    /**
     * 批量指派催收人员
     *
     * @param params
     * @return
     */
    void orderAllotUsers(Map<String, Object> params, Long... ids);
    /**
     *  根据主键获取催收订单
     * @param id
     * @return UrgeRepayOrder
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 10:58
     * */
    UrgeRepayOrder getById(Long id);
    /**
     * 导出查询
     *
     * @param params
     * @return
     */
    List listUrgeRepayOrder(Map<String, Object> params);
    /**
     * 催收导出查询
     *
     * @param params
     * @return
     */
    List listUrgeLog(Map<String, Object> params);

    /**
     * 修改逾期信息
     *
     * @param uroMap
     */
    int updateLate(Map<String, Object> uroMap);

    /**
     * 查询催收订单
     *
     * @param borrowId
     * @return
     */
    UrgeRepayOrder findByBorrowId(long borrowId);
}
