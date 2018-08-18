package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.common.exception.ManageException;
import com.adpanshi.cashloan.manage.cl.enums.BorrowEnum;
import com.adpanshi.cashloan.manage.cl.mapper.BorrowMapper;
import com.adpanshi.cashloan.manage.cl.mapper.BorrowRepayMapper;
import com.adpanshi.cashloan.manage.cl.mapper.UrgeRepayOrderMapper;
import com.adpanshi.cashloan.manage.cl.mapper.UserBaseInfoMapper;
import com.adpanshi.cashloan.manage.cl.model.*;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowModel;
import com.adpanshi.cashloan.manage.cl.model.expand.UrgeRepayCountModel;
import com.adpanshi.cashloan.manage.cl.model.expand.UrgeRepayOrderModel;
import com.adpanshi.cashloan.manage.cl.service.UrgeRepayOrderService;
import com.adpanshi.cashloan.manage.core.common.context.ExportConstant;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 催收订单serviceImpl
 * @date 2018/8/1 20:34
 */
@Service("urgeRepayOrderService")
public class UrgeRepayOrderServiceImpl implements UrgeRepayOrderService{
    private static final Logger logger = LoggerFactory.getLogger(UrgeRepayOrderServiceImpl.class);
    @Resource
    private UrgeRepayOrderMapper urgeRepayOrderMapper;
    @Resource
    private BorrowMapper borrowMapper;
    @Resource
    private BorrowRepayMapper borrowRepayMapper;
    @Resource
    private UserBaseInfoMapper userBaseInfoMapper;

    @Override
    public Page<UrgeRepayOrderModel> list(Map<String, Object> params, Integer current, Integer pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<UrgeRepayOrderModel>) urgeRepayOrderMapper.listUrgeTotalOrder(params);
    }

    @Override
    public Page<UrgeRepayOrderModel> listModel(Map<String, Object> params, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<UrgeRepayOrderModel>) urgeRepayOrderMapper.listModel(params);
    }

    @Override
    public Page<UrgeRepayCountModel> urgeCount(Map<String, Object> params,
                                               int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<UrgeRepayCountModel>) urgeRepayOrderMapper.urgeCount(params);
    }

    @Override
    public Page<UrgeRepayCountModel> memberCount(Map<String, Object> params, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<UrgeRepayCountModel>) urgeRepayOrderMapper.memberCount(params);
    }

    @Override
    public Page<UrgeRepayCountModel> orderCount(Map<String, Object> params,
                                                int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<UrgeRepayCountModel>) urgeRepayOrderMapper.orderCount(params);
    }
    @Override
    public Page<UrgeRepayCountModel> memberDayCount(Map<String, Object> params,
                                                    int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<UrgeRepayCountModel>) urgeRepayOrderMapper.memberDayCount(params);
    }

    @Override
    public Page<UrgeRepayOrderModel> listManage(Map<String, Object> params, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<UrgeRepayOrderModel>) urgeRepayOrderMapper.listManage(params);
    }

    @Override
    public int updateLate(Long id, String state) {
        UrgeRepayOrder order  = new UrgeRepayOrder();
        order.setId(id);
        order.setState(state);
        return urgeRepayOrderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public void saveOrder(Long id) throws ManageException {
        Borrow b = borrowMapper.selectByPrimaryKey(id);
        //借款信息不存在
        if (null == b) {
            throw new ManageException(ManageExceptionEnum.MANAGE_COLL_BORROW_NOTEXIST_CODE_VALUE);
        }
        //是否逾期标判断
        if (!BorrowModel.STATE_DELAY.equals(b.getState())) {
            throw new ManageException(ManageExceptionEnum.MANAGE_COLL_BORROW_NOTOVERDUE_CODE_VALUE);
        }
        UrgeRepayOrderExample orderExample = new UrgeRepayOrderExample();
        orderExample.createCriteria().andBorrowIdEqualTo(b.getId());
        List<UrgeRepayOrder> list = urgeRepayOrderMapper.selectByExample(orderExample);
        //已存在催收订单中，请勿重复添加
        if (null != list && list.size() > 0) {
            throw new ManageException(ManageExceptionEnum.MANAGE_COLL_BORROW_COLLEXIST_CODE_VALUE);
        }
        UrgeRepayOrder order = new UrgeRepayOrder();
        UserBaseInfoExample baseInfoExample = new UserBaseInfoExample();
        baseInfoExample.createCriteria().andUserIdEqualTo(b.getUserId());
        List<UserBaseInfo> info1s = userBaseInfoMapper.selectByExample(baseInfoExample);
        if (info1s.size() > 0) {
            UserBaseInfo user = info1s.get(0);
            order.setPhone(user.getPhone());
            order.setBorrowName(user.getRealName());
        }
        BorrowRepayExample example = new BorrowRepayExample();
        example.createCriteria().andBorrowIdEqualTo(b.getId());
        List<BorrowRepay> infos = borrowRepayMapper.selectByExample(example);
        if (infos.size() > 0) {
            BorrowRepay br = infos.get(0);
            order.setAmount(br.getAmount());
            order.setRepayTime(br.getRepayTime());
            order.setPenaltyDay(Long.valueOf(br.getPenaltyDay()));
            order.setPenaltyAmout(br.getPenaltyAmout());
            order.setLevel(UrgeRepayOrderModel.getLevelByDay(Long.valueOf(br.getPenaltyDay())));
        }
        order.setState(UrgeRepayOrderModel.STATE_ORDER_PRE);
        order.setCreateTime(new Date());
        order.setCount(Long.valueOf(0));
        order.setBorrowId(b.getId());
        order.setBorrowTime(b.getCreateTime());
        order.setTimeLimit(b.getTimeLimit());
        urgeRepayOrderMapper.insertSelective(order);
    }

    @Override
    public boolean isCollector(Long id) {
        UrgeRepayOrder uro = urgeRepayOrderMapper.selectByPrimaryKey(id);
        if (uro == null) {
            return false;
        }
        //是待催收、催收中、承诺还款、坏账则不更改状态
        if ((UrgeRepayOrderModel.STATE_ORDER_WAIT.equals(uro.getState())
                || UrgeRepayOrderModel.STATE_ORDER_URGE.equals(uro.getState())
                || UrgeRepayOrderModel.STATE_ORDER_PROMISE.equals(uro.getState())
                || UrgeRepayOrderModel.STATE_ORDER_BAD.equals(uro.getState())
                ||UrgeRepayOrderModel.STATE_ORDER_SUCCESS.equals(uro.getState()))) {
            return true;
        }
        return false;
    }

    @Override
    public int orderAllotUser(Map<String, Object> params) {
        return urgeRepayOrderMapper.updateSelective(params);
    }

    @Override
    public void orderAllotUsers(Map<String, Object> params, Long... idList) {
        String state = String.valueOf(params.get("state"));
        for (Long id : idList) {
            if (!isCollector(id)) {
                params.put("state", state);
            } else {
                params.put("state", "");
            }
            params.put("id", id);
            urgeRepayOrderMapper.updateSelective(params);
        }
    }

    @Override
    public UrgeRepayOrder getById(Long id) {
        return urgeRepayOrderMapper.selectByPrimaryKey(id);
    }

    @Override
    public List listUrgeRepayOrder(Map<String, Object> params) {
        params.put("totalCount", ExportConstant.TOTAL_LIMIT);
        List<UrgeRepayOrder> list = urgeRepayOrderMapper.listSelective(params);
        for (UrgeRepayOrder uro : list) {
            uro.setState(UrgeRepayOrderModel.change(uro.getState()));
        }
        return list;
    }

    @Override
    public List listUrgeLog(Map<String, Object> params) {
        params.put("totalCount", ExportConstant.TOTAL_LIMIT);
        List<UrgeRepayOrderModel> list = urgeRepayOrderMapper.listModel(params);
        for (UrgeRepayOrderModel uroModel : list) {
            uroModel.setState(UrgeRepayOrderModel.change(uroModel.getState()));
        }
        return list;
    }

    @Override
    public int updateLate(Map<String, Object> uroMap) {
        return urgeRepayOrderMapper.updateSelective(uroMap);
    }

    @Override
    public UrgeRepayOrder findByBorrowId(long borrowId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("borrowId", borrowId);
        UrgeRepayOrderExample example = new UrgeRepayOrderExample();
        example.createCriteria().andBorrowIdEqualTo(borrowId);
        List<UrgeRepayOrder> orders = urgeRepayOrderMapper.selectByExample(example);
        if(orders.size() > 0){
            return orders.get(0);
        } else {
            return null;
        }
    }
}
