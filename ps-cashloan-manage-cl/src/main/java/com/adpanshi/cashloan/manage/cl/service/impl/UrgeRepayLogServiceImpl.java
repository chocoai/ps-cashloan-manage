package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.enums.BorrowEnum;
import com.adpanshi.cashloan.manage.cl.mapper.BorrowMapper;
import com.adpanshi.cashloan.manage.cl.mapper.BorrowProgressMapper;
import com.adpanshi.cashloan.manage.cl.mapper.UrgeRepayLogMapper;
import com.adpanshi.cashloan.manage.cl.mapper.UrgeRepayOrderMapper;
import com.adpanshi.cashloan.manage.cl.model.*;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowModel;
import com.adpanshi.cashloan.manage.cl.model.expand.UrgeRepayOrderModel;
import com.adpanshi.cashloan.manage.cl.service.UrgeRepayLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Vic Tang
 * @Description: 催收反馈serviceImpl
 * @date 2018/8/1 20:34
 */
@Service("urgeRepayLogService")
public class UrgeRepayLogServiceImpl implements UrgeRepayLogService{
    private static final Logger logger = LoggerFactory.getLogger(UrgeRepayLogServiceImpl.class);
    @Resource
    private UrgeRepayLogMapper urgeRepayLogMapper;
    @Resource
    private UrgeRepayOrderMapper urgeRepayOrderMapper;
    @Resource
    private BorrowMapper borrowMapper;
    @Resource
    private BorrowProgressMapper borrowProgressMapper;

    @Override
    public Page<UrgeRepayLog> getLogByParam(Long dueId, Integer current, Integer pageSize) {
        PageHelper.startPage(current, pageSize);
        UrgeRepayLogExample example = new UrgeRepayLogExample();
        example.createCriteria().andDueIdEqualTo(dueId);
        return (Page<UrgeRepayLog>)urgeRepayLogMapper.selectByExample(example);
    }

    @Override
    public void saveOrderInfo(UrgeRepayLog urgeRepayLog, UrgeRepayOrder order) {
        if (StringUtils.isEmpty(urgeRepayLog.getState())) {
            urgeRepayLog.setState(UrgeRepayOrderModel.STATE_ORDER_URGE);
        }
        urgeRepayLog.setDueId(order.getId());
        urgeRepayLog.setBorrowId(order.getBorrowId());
        urgeRepayLog.setUserId(order.getUserId());
        //更新催收记录
        urgeRepayLogMapper.insertSelective(urgeRepayLog);
        //更新催收订单进度
        order.setCount(order.getCount() + 1);
        order.setState(urgeRepayLog.getState());
        urgeRepayOrderMapper.updateByPrimaryKey(order);
        if (order.getState().equals(UrgeRepayOrderModel.STATE_ORDER_BAD)) {
            //更新借款状态
            Borrow borrow = new Borrow();
            borrow.setId(order.getBorrowId());
            borrow.setState(UrgeRepayOrderModel.STATE_ORDER_BAD);
            borrowMapper.updateByPrimaryKeySelective(borrow);
            //添加借款进度
            BorrowProgress bp = new BorrowProgress();
            bp.setBorrowId(order.getBorrowId());
            bp.setUserId(order.getUserId());
            bp.setRemark(BorrowModel.convertBorrowRemark(BorrowModel.STATE_BAD));
            bp.setState(BorrowModel.STATE_BAD);
            bp.setCreateTime(new Date());
            borrowProgressMapper.insertSelective(bp);
        }
    }
}
