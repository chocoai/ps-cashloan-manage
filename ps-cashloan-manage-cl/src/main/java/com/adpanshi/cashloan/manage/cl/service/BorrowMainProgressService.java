package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.BorrowMainProgress;

import java.util.List;

/**
 * @author Vic Tang
 * @Description: 主订单进度service
 * @date 2018/8/3 14:44
 */
public interface BorrowMainProgressService {
    /**
     *  添加主订单进度
     * @param borrowMainProgress
     * @return void
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 14:47
     * */
    void insert(BorrowMainProgress borrowMainProgress);
    /**
     *  根据borrowMainId查询主订单进度
     * @param borrowMainId
     * @return List<BorrowMainProgress>
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 19:26
     * */
    List<BorrowMainProgress> getProcessByMainId(Long borrowMainId);
}
