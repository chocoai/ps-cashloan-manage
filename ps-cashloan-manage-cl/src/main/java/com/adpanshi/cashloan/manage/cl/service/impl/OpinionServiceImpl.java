package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.OpinionMapper;
import com.adpanshi.cashloan.manage.cl.model.Opinion;
import com.adpanshi.cashloan.manage.cl.model.expand.OpinionModel;
import com.adpanshi.cashloan.manage.cl.service.OpinionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 用户消息反馈serviceImpl
 * @date 2018/8/3 23:26
 */
@Service("opinionService")
public class OpinionServiceImpl implements OpinionService {
    private static final Logger logger = LoggerFactory.getLogger(OpinionServiceImpl.class);
    @Resource
    private OpinionMapper opinionMapper;
    @Override
    public Page<OpinionModel> page(Map<String, Object> params, Integer current, Integer pageSize) {
        PageHelper.startPage(current, pageSize);
        List<OpinionModel> list = opinionMapper.listModel(params);
        for (int i = 0; i < list.size(); i++) {
            OpinionModel opinionModel = list.get(i);
            if(OpinionModel.STATE_WAITE_CONFIRM.equals(opinionModel.getState())){
                opinionModel.setStateStr("待确认");
            } else if(OpinionModel.STATE_CONFIRMED.equals(opinionModel.getState())){
                opinionModel.setStateStr("已确认");
            }
            list.set(i, opinionModel);
        }
        return (Page<OpinionModel>)list;
    }

    @Override
    public Opinion getById(Long id) {
        return opinionMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateSelective(Opinion opinion) {
        return opinionMapper.updateByPrimaryKeySelective(opinion);
    }
}
