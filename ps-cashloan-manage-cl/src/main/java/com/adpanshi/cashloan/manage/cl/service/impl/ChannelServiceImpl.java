package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.ChannelMapper;
import com.adpanshi.cashloan.manage.cl.model.Channel;
import com.adpanshi.cashloan.manage.cl.service.ChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Vic Tang
 * @Description: 渠道serviceImpl
 * @date 2018/8/2 11:16
 */
@Service("channelService")
public class ChannelServiceImpl implements ChannelService{
    private static final Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);
    @Resource
    private ChannelMapper channelMapper;
    @Override
    public List<Channel> listChannel() {
        return channelMapper.listChannel();
    }

    @Override
    public Channel getById(Long id) {
        return channelMapper.selectByPrimaryKey(id);
    }
}
