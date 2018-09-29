package com.adpanshi.cashloan.manage.cl.mapper;

import com.adpanshi.cashloan.common.mapper.RDBatisDao;
import com.adpanshi.cashloan.manage.cl.model.UserCallRecord;
import org.apache.ibatis.annotations.Param;
@RDBatisDao
public interface UserCallRecordMapper {

    /**
     *  获取用户最新的通话记录
     *
     * @param tableName
     * @param userId
     * @return UserCallRecord
     * @throws
     * @author Vic Tang
     * @date 2018/8/15 15:53
     * */
    UserCallRecord getByUserId(@Param("tableName") String tableName, @Param("userId")Long userId);
    /**
     * 根据表名查询表数量
     * @param tableName
     * @return
     */
    int countTable(String tableName);
}