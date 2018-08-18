package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.PayLogMapper;
import com.adpanshi.cashloan.manage.cl.model.PayLog;
import com.adpanshi.cashloan.manage.cl.model.expand.PayLogModel;
import com.adpanshi.cashloan.manage.cl.service.PayLogService;
import com.adpanshi.cashloan.manage.core.common.context.ExportConstant;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tool.util.StringUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 支付记录serviceImpl
 * @date 2018/8/3 11:51
 */
@Service("payLogService")
public class PayLogServiceImpl implements PayLogService{
    private static final Logger logger = LoggerFactory.getLogger(PayLogServiceImpl.class);
    @Resource
    private PayLogMapper payLogMapper;
    @Override
    public Page<PayLogModel> page(Integer current, Integer pageSize, Map<String, Object> searchMap) {
        PageHelper.startPage(current, pageSize);
        if (null != searchMap && searchMap.size() > 0)
        {
            String payState = (String)searchMap.get("state") ;
            if (PayLogModel.STATE_PENDING_REVIEW.equals(payState)){
                searchMap.put("isVerify",true);
            }
        }
        Page<PayLogModel> page = (Page<PayLogModel>) payLogMapper.page(searchMap);
        return page;
    }

    @Override
    public PayLog findPayLogByLastOrderNoWithBorrowId(String orderNo, String borrowId) {
        if(StringUtil.isBlank(orderNo)) {
            return null;
        }
        return payLogMapper.findPayLogByLastOrderNoWithBorrowId(orderNo, borrowId);
    }

    @Override
    public boolean updateSelective(PayLog record) {
        int result  = payLogMapper.updateByPrimaryKeySelective(record);
        if(result > 0L){
            return true;
        }
        return false;
    }

    @Override
    public List listPayLog(Map<String, Object> searchMap) {
        String type  = StringUtil.isNull(searchMap.get("type"));
        String[] typeArray = type.split(",");

        List<String> typeList =  new ArrayList<String>();
        for (String typeStr : typeArray) {
            if (StringUtil.isNotBlank(typeStr)) {
                typeList.add(typeStr);
            }
        }
        searchMap.put("type", typeList);
        searchMap.put("totalCount", ExportConstant.TOTAL_LIMIT);
        List<PayLogModel> list = payLogMapper.page(searchMap);
        return list;
    }
}
