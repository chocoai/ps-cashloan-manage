package com.adpanshi.cashloan.manage.cl.service.impl;


import com.adpanshi.cashloan.manage.cl.mapper.expand.OverdueStatisticMapper;
import com.adpanshi.cashloan.manage.cl.service.OverdueStatisticService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by 8631 on 2018/8/24.
 */
@Service("OverdueStatisticService")
public class OverdueStatisticServiceImpl implements OverdueStatisticService {
    @Resource
    private OverdueStatisticMapper overdueStatisticMapper;

    @Override
    public Map<String, Object> getStatisticIndex() {
        return overdueStatisticMapper.getStatisticIndex();
    }
}
