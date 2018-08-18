package com.adpanshi.cashloan.manage.cl.mapper.expand;



import com.adpanshi.cashloan.common.mapper.RDBatisDao;
import com.adpanshi.cashloan.manage.cl.pojo.BorrowAndRepayModel;

import java.util.List;
import java.util.Map;

/**
 * 逾期计算模型拼装
 *
 * @author yecy
 * @date 2017/12/20 10:11
 */
@RDBatisDao
public interface BorrowRepayModelMapper {

   List<BorrowAndRepayModel> findExpireToRepay(Map<String, Object> map);
}
