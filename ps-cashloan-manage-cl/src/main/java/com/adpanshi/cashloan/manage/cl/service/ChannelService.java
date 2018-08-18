package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.manage.cl.model.Channel;

import java.util.List;

/**
 * @author Vic Tang
 * @Description: 渠道service
 * @date 2018/8/2 11:16
 */
public interface ChannelService {
    /**
     * 查出所有渠道信息
     *
     * @return List<Channel>
     */
    List<Channel> listChannel();
    /**
     *  根据主键获取渠道商信息
     * @param id
     * @return Channel
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 9:36
     * */
    Channel getById(Long id);
}
