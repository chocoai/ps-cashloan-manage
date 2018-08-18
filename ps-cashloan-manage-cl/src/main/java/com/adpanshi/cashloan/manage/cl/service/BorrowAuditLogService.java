package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.BorrowAuditLog;
import com.adpanshi.cashloan.manage.cl.model.BorrowAuditLogWithBLOBs;

/**
 * @author Vic Tang
 * @Description: 审核日志service
 * @date 2018/8/2 11:15
 */
public interface BorrowAuditLogService {
    /**
     * 添加人工复审操作记录
     * @param borrowId
     * @param auditId,
     * @param auditName
     * @return void
     * @throws
     * @author Vic Tang
     * @date 2018/8/2 20:35
     * */
    void addLog(long borrowId, long auditId, String auditName);
    /**
     *  查询人工复审操作记录明细
     * @param borrowId
     * @return BorrowAuditLog
     * @throws
     * @author Vic Tang
     * @date 2018/8/2 21:29
     * */
    BorrowAuditLogWithBLOBs findLogs(Long borrowId);
}
