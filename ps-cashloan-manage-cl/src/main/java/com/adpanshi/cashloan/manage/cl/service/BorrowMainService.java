package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.common.exception.ManageException;
import com.adpanshi.cashloan.manage.arc.model.SysUser;
import com.adpanshi.cashloan.manage.cl.model.BorrowMain;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowMainModel;

import java.util.Date;
import java.util.List;
import java.util.Map; /**
 * @author Vic Tang
 * @Description: 主订单service
 * @date 2018/8/2 11:48
 */
public interface BorrowMainService {
    /**
     * 查询指定条件所有订单信息
     * @param searchParams
     * @param pageSize
     * @param current
     * @return
     */
    List<BorrowMainModel> selectBorrowList(Map<String, Object> searchParams, int current, int pageSize);

    /**
     * @Description:  查询还款信息
     * @Param: id
     * @return: java.util.Map
     * @Author: Mr.Wange
     * @Date: 2018/7/24
     */
    Map qryRepayLog(Long id);
    /**
     *  根据主键获取主订单
     * @param id
     * @return BorrowMain
     * @throws
     * @author Vic Tang
     * @date 2018/8/2 20:33
     * */
    BorrowMain getById(Long id);

    /**
     * 支付时更新状态
     *
     * @return
     */
    void updatePayState(Long borrowId, String state, Date loanTime, Date repayTime);
    /**
     * 分配:订单批量指派信审人员
     * @param sysUser
     * @param borrowMainIds
     * @param userId
     * @param userName
     * @date : 2018-1-03 19:41
     * @author: nmnl
     */
    Map<String, Object> orderAllotSysUsers(SysUser sysUser, Long [] borrowMainIds,Long userId,String userName);
    /**
     * 修改借款状态和备注
     * @param id
     * @param state
     * @return
     */
    int modifyStateAndRemark(long id, String state,String remark);
    /**
     *  人工复审
     * @return java.lang.String
     * @throws
     * @author Vic Tang
     * @date 2018/8/4 1:09
     * */
    String manualVerifyBorrow(Long borrowMainId, String state, String remark, Long userId, String realName, String orderView) throws ManageException;
}
