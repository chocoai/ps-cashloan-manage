package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.BorrowMainProgressMapper;
import com.adpanshi.cashloan.manage.cl.model.BorrowMainProgress;
import com.adpanshi.cashloan.manage.cl.model.BorrowMainProgressExample;
import com.adpanshi.cashloan.manage.cl.service.BorrowMainProgressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vic Tang
 * @Description: 主订单进度serviceImpl
 * @date 2018/8/3 14:45
 */
@Service("borrowMainProgressService")
public class BorrowMainProgressServiceImpl implements BorrowMainProgressService {
    private static final Logger logger = LoggerFactory.getLogger(BorrowMainProgressServiceImpl.class);
    @Resource
    private BorrowMainProgressMapper borrowMainProgressMapper;
    @Override
    public void insert(BorrowMainProgress borrowMainProgress) {
        borrowMainProgressMapper.insertSelective(borrowMainProgress);
    }

    @Override
    public List<BorrowMainProgress> getProcessByMainId(Long borrowMainId) {
        if (borrowMainId == null) {
            return new ArrayList<>();
        }
        BorrowMainProgressExample example = new BorrowMainProgressExample();
        example.createCriteria().andBorrowIdEqualTo(borrowMainId);
        return borrowMainProgressMapper.selectByExample(example);
    }
}
