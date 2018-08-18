package com.adpanshi.cashloan.manage.cl.service.impl;

import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.common.exception.ManageException;
import com.adpanshi.cashloan.manage.arc.mapper.SysUserMapper;
import com.adpanshi.cashloan.manage.arc.model.SysUser;
import com.adpanshi.cashloan.manage.cl.mapper.ExamineDictDetailMapper;
import com.adpanshi.cashloan.manage.cl.mapper.UserExamineMapper;
import com.adpanshi.cashloan.manage.cl.model.ExamineDictDetail;
import com.adpanshi.cashloan.manage.cl.model.ExamineDictDetailExample;
import com.adpanshi.cashloan.manage.cl.model.UserExamine;
import com.adpanshi.cashloan.manage.cl.service.UserExamineService;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import com.adpanshi.cashloan.manage.core.common.util.DateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 信审人员serviceImpl
 * @date 2018/8/2 11:19
 */
@Service("userExamineService")
public class UserExamineServiceImpl implements UserExamineService {
    private static final Logger logger = LoggerFactory.getLogger(UserExamineServiceImpl.class);
    @Resource
    private UserExamineMapper userExamineMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private ExamineDictDetailMapper examineDictDetailMapper;

    @Override
    public Page<UserExamine> listUserExamineInfo(Map<String, Object> params, Integer current, Integer pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<UserExamine>) userExamineMapper.listUserExamineInfo(params);
    }

    @Override
    public boolean updateUserExamineInfo(AuthUserRole sysUser, Long id, String status) {
        //获取信审专员信息，更新操作记录
        UserExamine userexam = userExamineMapper.selectByPrimaryKey(id);
        userexam.setUpdateTime(DateUtil.getNow());
        userexam.setStatus(status);
        userexam.setManageId(sysUser.getUserId());
        userexam.setManageName(sysUser.getRealName());
        return userExamineMapper.updateByPrimaryKeySelective(userexam) > 0;
    }

    @Override
    public List<Map<String, Object>> listadduser() {
        return userExamineMapper.listadduser();
    }

    @Override
    public void addUserExamInfo(Long... userIds) throws ManageException {
        Map<String,Object> params = new HashMap<>();
        params.put("userIds",userIds);
        params.put("nid","LetteronCommissioner");
        List<SysUser> sysUserList = sysUserMapper.getSysUserByMap(params);
        for(SysUser sysUser :sysUserList) {
            //判断信审人员是否已经存在
            Map<String, Object> examap = new HashMap<String, Object>();
            examap.put("userId", sysUser.getId());
            List<UserExamine> userExaminelist = userExamineMapper.listUserExamineInfo(examap);
            if (userExaminelist != null && !userExaminelist.isEmpty()) {
                throw new ManageException(ManageExceptionEnum.MANAGE_EXAMINE_EXIST_CODE_VALUE);
            }
            UserExamine userExamine = new UserExamine();
            userExamine.setUserId(sysUser.getId());
            userExamine.setUserName(sysUser.getName());
            userExamine.setCreateTime(DateUtil.getNow());
            userExamine.setStatus("1");
            userExamineMapper.insertSelective(userExamine);
        }
    }

    @Override
    public List<ExamineDictDetail> selectBorrowExamineDictDetailInfo(String codeType) {
        ExamineDictDetailExample example = new ExamineDictDetailExample();
        example.createCriteria().andStateEqualTo("10");
        return examineDictDetailMapper.selectByExample(example);
    }

    @Override
    public List<UserExamine> listUserExamineInfo1() {
        return userExamineMapper.listUserExamineInfo(new HashMap<String, Object>());
    }

    @Override
    public List<UserExamine> selectAllotMan(){
        //获取状态为启用的信审人员信息
        Map<String,Object> userParam = new HashMap<String, Object>();
        userParam.put("status", "1");
        return userExamineMapper.listUserExamineInfo(userParam);
    }

    @Override
    public Page<ExamineDictDetail> selectExamineDictDetailInfo(Map<String, Object> params, Integer current, Integer pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<ExamineDictDetail>)examineDictDetailMapper.findExamineDictDetailInfo(params);
    }

    @Override
    public int updateExamineDictDetail(Long id, String realName, String value) {
        ExamineDictDetail exma = examineDictDetailMapper.selectByPrimaryKey(id);
        exma.setMender(realName);
        exma.setMenderTime(DateUtil.getNow());
        //修改数据 || 启用/禁用
        if(StringUtils.isNotEmpty(value)) {
            exma.setValue(value);
        }else {
            exma.setState(exma.getState().equals("10") ? "20" : "10");
        }
        return examineDictDetailMapper.updateByPrimaryKeySelective(exma);
    }

    @Override
    public int saveExamineDictDetail(ExamineDictDetail exma) {
        //查询最大code值
        Long code = examineDictDetailMapper.selectMaxCode();
        if(code == null){
            code = 1L;
        }else{
            code +=  1;
        }
        //新增信息
        exma.setCode(code);
        exma.setState("10");//枚举
        exma.setCreateTime(DateUtil.getNow());
        return examineDictDetailMapper.insert(exma);
    }
}
