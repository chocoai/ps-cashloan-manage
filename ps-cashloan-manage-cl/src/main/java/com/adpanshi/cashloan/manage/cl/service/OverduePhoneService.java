package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.OverduePhone;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map; /**
 * @author Vic Tang
 * @Description: 催收号字典
 * @date 2018/8/3 19:55
 */
public interface OverduePhoneService {
    /**
     * 分页查询
     * 支持customer_name & phone 的模糊查询（like），createUser完全匹配(=)，id & create_time 暂不匹配
     * @param current
     * @param pageSize
     * @param searchMap
     * @return
     */
    Page<OverduePhone> list(int current, int pageSize, Map<String, Object> searchMap);
    /**
     *  修改
     * @param overduePhone
     * @return int
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 20:43
     * */
    int update(OverduePhone overduePhone);
    /**
     *  新增
     * @param overduePhone
     * @return int
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 20:43
     * */
    int save(OverduePhone overduePhone);
    /**
     *  删除
     * @param ids
     * @return int
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 20:43
     * */
    int deleteByIds(List<Long> ids);
}
