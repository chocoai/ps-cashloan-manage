package com.adpanshi.cashloan.manage.cl.service;


import com.adpanshi.cashloan.manage.cl.model.UserContactsMatchDict;
import com.github.pagehelper.Page;

import java.util.Map; /**
 * @author Vic Tang
 * @Description: 通讯录匹配字典
 * @date 2018/8/3 19:53
 */
public interface UserContactsMatchDictService {
    Page<UserContactsMatchDict> list(Integer current, Integer pageSize, Map<String, Object> searchMap);
    /**
     *  修改
     * @param contactMatchDict
     * @return void
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 20:32
     * */
    int update(UserContactsMatchDict contactMatchDict);
    /**
     *  新增
     * @param contactMatchDict
     * @return void
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 20:33
     * */
    int save(UserContactsMatchDict contactMatchDict);
    /**
     *  删除字典
     * @param id
     * @return void
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 20:34
     * */
    int delById(Long id);
}
