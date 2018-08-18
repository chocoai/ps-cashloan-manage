package com.adpanshi.cashloan.manage.cl.service;


import com.adpanshi.cashloan.manage.cl.model.expand.BorrowRepayLogModel;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 还款记录service
 * @date 2018/8/1 10:30
 */
public interface BorrowRepayLogService {
    /**
     *  查询还款记录列表
     * @param params
     * @param currentPage
     * @param pageSize
     * @return Page<BorrowRepayLogModel> 还款记录分页列表
     * @throws
     * @author Vic Tang
     * @date 2018/8/1 10:40
     * */
    Page<BorrowRepayLogModel> listModel(Map<String, Object> params, Integer currentPage, Integer pageSize);
    /**
     * 导出文件查询
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    List listExport(Map<String, Object> params);
}
