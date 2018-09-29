
package com.adpanshi.cashloan.manage.cl.mapper;

import com.adpanshi.cashloan.common.mapper.RDBatisDao;
import com.adpanshi.cashloan.manage.cl.domain.CreditsUpgradeLog;

import java.util.List;
import java.util.Map;

/**
 * @category 用户额度提升日志表
 * @author 8470
 * @Since 2011-2017
 * @create 2017-10-30 20:46:14
 * @history
 */
@RDBatisDao
public interface CreditsUpgradeLogMapper {

    /**
     * 根据父类ID关联查询
     * @param parentId
     * @return
     */
    List<CreditsUpgradeLog> listSelectByParentId(Long parentId);

    /**
     * 查询最近的一条记录
     * @param paramMap
     * @return
     */
    CreditsUpgradeLog findLatestRecord(Map<String, Object> paramMap);

    /**
     * 插入数据
     * @param creditsUpgradeLog
     * @return
     */
    int save(CreditsUpgradeLog creditsUpgradeLog);

}
