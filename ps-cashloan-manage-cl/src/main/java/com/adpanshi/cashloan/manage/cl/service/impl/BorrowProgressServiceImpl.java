package com.adpanshi.cashloan.manage.cl.service.impl;




import com.adpanshi.cashloan.manage.cl.mapper.BorrowProgressMapper;
import com.adpanshi.cashloan.manage.cl.model.BorrowProgress;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowProgressModel;
import com.adpanshi.cashloan.manage.cl.service.BorrowProgressService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 借款订单进度serviceImpl
 * @date 2018/8/1 14:41
 */
@Service("borrowProgressService")
public class BorrowProgressServiceImpl implements BorrowProgressService{
    private static final Logger logger = LoggerFactory.getLogger(BorrowProgressServiceImpl.class);
    @Resource
    private BorrowProgressMapper borrowProgressMapper;


    @Override
    public Page<BorrowProgressModel> listModel(Map<String, Object> params, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<BorrowProgressModel> list = borrowProgressMapper.listModel(params);
        return (Page<BorrowProgressModel>)list;
    }

    @Override
    public int saveAll(List<BorrowProgress> processList) {
        if (CollectionUtils.isEmpty(processList)){
            return 0;
        }
        return borrowProgressMapper.saveAll(processList);
    }

    @Override
    public List<BorrowProgress> findProcessByState(Long borrowId, Collection<String> stateList) {
        if (CollectionUtils.isEmpty(stateList) || borrowId == null){
            return new ArrayList<>();
        }
        return borrowProgressMapper.findProcessByState(borrowId,stateList);
    }

    @Override
    public int save(BorrowProgress bp) {
        return borrowProgressMapper.insertSelective(bp);
    }
}
