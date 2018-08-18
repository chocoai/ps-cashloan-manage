package com.adpanshi.cashloan.manage.cl.service.impl;



import com.adpanshi.cashloan.manage.cl.mapper.*;
import com.adpanshi.cashloan.manage.cl.model.*;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowModel;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowProgressModel;
import com.adpanshi.cashloan.manage.cl.service.BorrowService;
import com.adpanshi.cashloan.manage.core.common.context.ExportConstant;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 借款订单serviceImpl
 * @date 2018/8/1 14:34
 */
@Service("borrowService")
public class BorrowServiceImpl implements BorrowService{
    private static final Logger logger = LoggerFactory.getLogger(BorrowServiceImpl.class);
    @Resource
    private BorrowMapper borrowMapper;
    @Resource
    private UserBaseInfoMapper userBaseInfoMapper;
    @Resource
    private BorrowProgressMapper borrowProgressMapper;
    @Resource
    private BorrowRepayMapper borrowRepayMapper;
    @Resource
    private BorrowRepayLogMapper borrowRepayLogMapper;
    @Resource
    private UrgeRepayOrderMapper urgeRepayOrderMapper;

    @Override
    public Page<BorrowModel> listBorrowModel(Map<String, Object> map, Integer current, Integer pageSize) {
        PageHelper.startPage(current, pageSize);
        List<BorrowModel> list = borrowMapper.listBorrowModel(map);
        return (Page<BorrowModel>) list;
    }

    @Override
    public int saveAll(List<Borrow> borrowList) {
        if (CollectionUtils.isEmpty(borrowList)) {
            return 0;
        }
        return borrowMapper.saveAll(borrowList);
    }

    @Override
    public List listBorrowOutModel(Map<String, Object> params) {
        params.put("totalCount", ExportConstant.TOTAL_LIMIT);
        List<BorrowModel> list = borrowMapper.listBorrowModel(params);
        for (BorrowModel mbm : list) {
            mbm.setState(BorrowModel.manageConvertBorrowState(mbm.getState()));
        }
        return list;
    }

    @Override
    public Borrow getById(Long borrowId) {
        return borrowMapper.selectByPrimaryKey(borrowId);
    }

    @Override
    public int modifyState(Long id, String state) {
        Borrow borrow = new Borrow();
        borrow.setId(id);
        borrow.setState(state);
        return borrowMapper.updateByPrimaryKeySelective(borrow);
    }

    @Override
    public List<Borrow> findBorrowByMap(Map<String, Object> searchMap) {
        List<Borrow> list = borrowMapper.listSelective(searchMap);
        return list;
    }

    @Override
    public BorrowModel getModelByBorrowId(Long borrowId) {
        return borrowMapper.getModelByBorrowId(borrowId) ;
    }
}
