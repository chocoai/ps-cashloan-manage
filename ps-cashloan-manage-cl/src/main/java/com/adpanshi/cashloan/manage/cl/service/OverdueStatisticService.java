package com.adpanshi.cashloan.manage.cl.service;

import java.util.Map;

/**
 * Created by 8631 on 2018/8/24.
 */
public interface OverdueStatisticService {
    /**
     *  统计首页逾期信息
     * @throws
     * @author Vic Tang
     * @date 2018/8/28 17:23
     * */
    Map<String,Object> getStatisticIndex();
}
