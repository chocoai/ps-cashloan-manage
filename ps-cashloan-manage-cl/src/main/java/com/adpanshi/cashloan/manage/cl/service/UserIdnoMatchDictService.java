package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.UserIdnoMatchDict;
import com.github.pagehelper.Page;

import java.util.Map; /**
 * @author Vic Tang
 * @Description: 身份省份匹配字典
 * @date 2018/8/3 19:54
 */
public interface UserIdnoMatchDictService {
    Page<UserIdnoMatchDict> list(Integer current, Integer pageSize, Map<String, Object> params);

    int save(UserIdnoMatchDict userIdnoMatchDict);

    int update(UserIdnoMatchDict userIdnoMatchDict);

    int updateState(Long id);
}
