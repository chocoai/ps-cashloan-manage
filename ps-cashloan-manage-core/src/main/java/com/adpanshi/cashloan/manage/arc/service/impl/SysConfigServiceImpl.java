package com.adpanshi.cashloan.manage.arc.service.impl;

import com.adpanshi.cashloan.manage.arc.mapper.SysConfigMapper;
import com.adpanshi.cashloan.manage.arc.model.SysConfig;
import com.adpanshi.cashloan.manage.arc.model.SysConfigExample;
import com.adpanshi.cashloan.manage.arc.service.SysConfigService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 系统参数serviceImpl
 * @date 2018/8/3 21:22
 */
@Service("sysConfigService")
public class SysConfigServiceImpl implements SysConfigService{
    public static final Logger logger = LoggerFactory.getLogger(SysConfigServiceImpl.class);
    @Resource
    private SysConfigMapper sysConfigMapper;
    @Override
    public Page<SysConfig> getSysConfigPageList(int currentPage, int pageSize, Map<String, Object> map) {
        PageHelper.startPage(currentPage, pageSize);
        return(Page<SysConfig>)sysConfigMapper.listSelective(map);
    }

    @Override
    public int updateSysConfig(SysConfig sysConfig) {
        return	sysConfigMapper.updateByPrimaryKeySelective(sysConfig);
    }

    @Override
    public SysConfig selectByCode(String code) {
        SysConfigExample example = new SysConfigExample();
        example.createCriteria().andCodeEqualTo(code);
        List<SysConfig> infos = sysConfigMapper.selectByExample(example);
        if(infos.size() > 0){
            return infos.get(0);
        } else {
          return null;
        }
    }

    @Override
    public int insertSysConfig(SysConfig sysConfig) {
        return sysConfigMapper.insertSelective(sysConfig);
    }

    @Override
    public List<SysConfig> findAll() {
        return sysConfigMapper.listSelective(null);
    }
}
