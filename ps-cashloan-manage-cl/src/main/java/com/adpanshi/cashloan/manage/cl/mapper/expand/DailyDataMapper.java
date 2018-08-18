package com.adpanshi.cashloan.manage.cl.mapper.expand;



import com.adpanshi.cashloan.common.mapper.RDBatisDao;
import com.adpanshi.cashloan.manage.cl.pojo.DailyData;
import com.adpanshi.cashloan.manage.cl.pojo.DayPassApr;

import java.util.List;
import java.util.Map;

/**
 * 新日报统计接口
 * Created by cc on 2017/7/31.
 */
@RDBatisDao
public interface DailyDataMapper {

    List<DailyData> dayData(Map<String, Object> params);

    void saveDayData();

    List<DayPassApr> dayApr(Map<String, Object> params);

}
