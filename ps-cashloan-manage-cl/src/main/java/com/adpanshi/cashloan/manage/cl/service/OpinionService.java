package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.Opinion;
import com.adpanshi.cashloan.manage.cl.model.expand.OpinionModel;
import com.github.pagehelper.Page;

import java.util.Map; /**
 * @author Vic Tang
 * @Description: 消息反馈service
 * @date 2018/8/3 23:25
 */
public interface OpinionService {
    Page<OpinionModel> page(Map<String, Object> params, Integer current, Integer pageSize);

    Opinion getById(Long id);

    int updateSelective(Opinion opinion);
}
