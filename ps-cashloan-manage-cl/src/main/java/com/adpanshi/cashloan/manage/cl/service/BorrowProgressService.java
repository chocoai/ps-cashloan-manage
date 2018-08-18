package com.adpanshi.cashloan.manage.cl.service;


import com.adpanshi.cashloan.manage.cl.model.BorrowProgress;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowProgressModel;
import com.github.pagehelper.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 订单进度service
 * @date 2018/8/1 14:39
 */
public interface BorrowProgressService {
    /**
     *  后台还款进度列表
     * @param params
     * @param currentPage
     * @param pageSize
     * @return Page<BorrowProgressModel>
     * @throws
     * @author Vic Tang
     * @date 2018/8/1 15:05
     * */
    Page<BorrowProgressModel> listModel(Map<String, Object> params, int currentPage, int pageSize);
    /**
     * 批量保存
     *
     * @param processList
     * @return
     * @author yecy 20171224
     */
    int saveAll(List<BorrowProgress> processList);
    /**
     * 查找借款进度中存在过借款类型为'$state'的流程
     * @param stateList
     * @return
     */
    List<BorrowProgress> findProcessByState(Long borrowId, Collection<String> stateList);

    int save(BorrowProgress bp);
}
