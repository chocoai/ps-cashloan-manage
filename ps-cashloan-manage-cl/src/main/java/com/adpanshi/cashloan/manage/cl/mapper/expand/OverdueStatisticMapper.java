package com.adpanshi.cashloan.manage.cl.mapper.expand;


import com.adpanshi.cashloan.common.mapper.RDBatisDao;
import java.util.Map;

/**
 * Created by 8631 on 2018/8/24.
 */
@RDBatisDao
public interface OverdueStatisticMapper {
    /**
     *  统计首页逾期信息
     * @throws
     * @author Vic Tang
     * @date 2018/8/28 17:24
     * */
    Map<String,Object> getStatisticIndex();
}
