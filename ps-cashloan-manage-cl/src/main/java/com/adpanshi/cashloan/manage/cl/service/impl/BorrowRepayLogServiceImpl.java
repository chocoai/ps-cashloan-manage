package com.adpanshi.cashloan.manage.cl.service.impl;




import com.adpanshi.cashloan.manage.cl.mapper.BorrowRepayLogMapper;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowRepayLogModel;
import com.adpanshi.cashloan.manage.cl.service.BorrowRepayLogService;
import com.adpanshi.cashloan.manage.core.common.context.ExportConstant;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 还款记录serviceImpl
 * @date 2018/8/1 10:31
 */
@Service("borrowRepayLogService")
public class BorrowRepayLogServiceImpl implements BorrowRepayLogService {
    @Resource
    private BorrowRepayLogMapper borrowRepayLogMapper;


    @Override
    public Page<BorrowRepayLogModel> listModel(Map<String, Object> params, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        if (null != params && params.size() > 0)
        {
            // 集合不为空则开始递归去除字符串的空格
            for(Map.Entry<String, Object>  entry : params.entrySet())
            {
                if(null != params.get(entry.getKey()) && params.get(entry.getKey()) != "") {
                    params.put(entry.getKey(), params.get(entry.getKey()).toString().trim());
                }
            }
        }
        List<BorrowRepayLogModel> list = borrowRepayLogMapper.listModel(params);
        return (Page<BorrowRepayLogModel>)list;
    }

    @Override
    public List listExport(Map<String, Object> params) {
        params.put("totalCount", ExportConstant.TOTAL_LIMIT);
        List<BorrowRepayLogModel> list = borrowRepayLogMapper.listModel(params);
        for (BorrowRepayLogModel m : list) {
            switch (m.getRepayWay()) {
                case "10":
                    m.setRepayWay("代扣");
                    break;
                case "20":
                    m.setRepayWay("银行卡转账");
                    break;
                case "30":
                    m.setRepayWay("支付宝转账");
                    break;
                case "40": // @author:nmnl @remarks: 增加主动还款.@date: 20170719
                    m.setRepayWay("线上主动还款");
                    break;
            }
        }
        return list;
    }
}
