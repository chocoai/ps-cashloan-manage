package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.manage.cl.mapper.QuartzInfoMapper;
import com.adpanshi.cashloan.manage.cl.mapper.QuartzLogMapper;
import com.adpanshi.cashloan.manage.cl.model.QuartzInfo;
import com.adpanshi.cashloan.manage.cl.model.QuartzInfoExample;
import com.adpanshi.cashloan.manage.cl.model.expand.QuartzInfoModel;
import com.adpanshi.cashloan.manage.cl.service.QuartzInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tool.util.DateUtil;
import tool.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 定时任务serviceImpl
 * @date 2018/8/3 23:54
 */
@Service("quartzInfoService")
public class QuartzInfoServiceImpl implements QuartzInfoService{
    private static final Logger logger = LoggerFactory.getLogger(QuartzInfoServiceImpl.class);
    @Resource
    private QuartzInfoMapper quartzInfoMapper;
    @Resource
    private QuartzLogMapper quartzLogMapper;
    @Override
    public List<QuartzInfo> list(Map<String, Object> result) {
        return quartzInfoMapper.listSelective(result);
    }

    @Override
    public Page<QuartzInfoModel> page(Map<String, Object> searchMap, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        List<QuartzInfoModel> list = quartzInfoMapper.page(searchMap);
        for (QuartzInfoModel quartzInfoModel : list) {
            String lastStartTime = quartzLogMapper.findLastTimeByQzInfoId(quartzInfoModel.getId());
            if (StringUtil.isNotBlank(lastStartTime)) {
                quartzInfoModel.setLastStartTime(DateUtil.valueOf(lastStartTime));
            }
        }
        return (Page<QuartzInfoModel>) list;
    }

    @Override
    public QuartzInfo getById(Long id) {
        return quartzInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean update(QuartzInfo info) {
        return quartzInfoMapper.updateByPrimaryKeySelective(info) > 0;
    }

    @Override
    public QuartzInfo findSelective(String code) {
        QuartzInfoExample example = new QuartzInfoExample();
        example.createCriteria().andCodeEqualTo(code);
        List<QuartzInfo> infos = quartzInfoMapper.selectByExample(example);
        if(infos.size() > 0){
            return infos.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean save(QuartzInfo quartzInfo) {
        return quartzInfoMapper.insertSelective(quartzInfo) > 0;
    }
}
