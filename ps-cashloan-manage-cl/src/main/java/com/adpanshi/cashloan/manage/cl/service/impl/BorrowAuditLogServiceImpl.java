package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.BorrowAuditLogMapper;
import com.adpanshi.cashloan.manage.cl.model.BorrowAuditLogExample;
import com.adpanshi.cashloan.manage.cl.model.BorrowAuditLogWithBLOBs;
import com.adpanshi.cashloan.manage.cl.service.BorrowAuditLogService;
import com.adpanshi.cashloan.manage.core.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Vic Tang
 * @Description: 审核日志serviceImpl
 * @date 2018/8/2 11:18
 */
@Service("borrowAuditLogService")
public class BorrowAuditLogServiceImpl implements BorrowAuditLogService {
    private static final Logger logger = LoggerFactory.getLogger(UrgeRepayOrderServiceImpl.class);
    @Resource
    private BorrowAuditLogMapper borrowAuditLogMapper;
    @Override
    public void addLog(long borrowId, long auditId, String auditName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String auditTime = sdf.format(new Date());
        BorrowAuditLogWithBLOBs borrowAuditLog = borrowAuditLogMapper.selectByPrimaryKey(borrowId);
        if (null == borrowAuditLog) {
            //新增记录
            List<String> list = new ArrayList<>();
            list.add(auditId + ":" + auditName + ":" + auditTime);
            String auditLog = JsonUtil.toString(list);
            BorrowAuditLogWithBLOBs bloBs = new BorrowAuditLogWithBLOBs();
            bloBs.setBorrowId(borrowId);
            bloBs.setAuditData(auditLog);
            bloBs.setCreateTime(new Date());
            borrowAuditLogMapper.insertSelective(bloBs);
        } else {
            //添加记录
            List list = JsonUtil.parse(borrowAuditLog.getAuditData(), List.class);
            list.add(0, auditId + ":" + auditName + ":" + auditTime);
            String auditLog = JsonUtil.toString(list);
            BorrowAuditLogWithBLOBs bloBs = new BorrowAuditLogWithBLOBs();
            bloBs.setAuditData(auditLog);
            bloBs.setUpdateTime(new Date());
            BorrowAuditLogExample example = new BorrowAuditLogExample();
            example.createCriteria().andBorrowIdEqualTo(borrowId);
            borrowAuditLogMapper.updateByExampleSelective(bloBs, example);
        }
    }

    @Override
    public BorrowAuditLogWithBLOBs findLogs(Long borrowId) {
        BorrowAuditLogExample example = new BorrowAuditLogExample();
        example.createCriteria().andBorrowIdEqualTo(borrowId);
        List<BorrowAuditLogWithBLOBs> infos =  borrowAuditLogMapper.selectByExampleWithBLOBs(example);
        if(infos.size() > 0){
            return infos.get(0);
        } else {
            return null;
        }
    }
}
