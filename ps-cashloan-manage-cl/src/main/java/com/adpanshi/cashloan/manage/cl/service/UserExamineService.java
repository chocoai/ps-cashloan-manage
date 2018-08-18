package com.adpanshi.cashloan.manage.cl.service;

import com.adpanshi.cashloan.common.exception.ManageException;
import com.adpanshi.cashloan.manage.cl.model.ExamineDictDetail;
import com.adpanshi.cashloan.manage.cl.model.UserExamine;
import com.adpanshi.cashloan.manage.core.common.pojo.AuthUserRole;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map; /**
 * @author Vic Tang
 * @Description: 信审人员serveice
 * @date 2018/8/2 11:15
 */
public interface UserExamineService {
    /**
     * 查询信审用户信息
     * @return
     */
    Page<UserExamine> listUserExamineInfo(Map<String, Object> params, Integer current, Integer pageSize);
    /**
     * 更新信审用户信息
     */
    boolean updateUserExamineInfo(AuthUserRole sysUser, Long id, String status);

    /**
     * 查询未添加信审人员
     * @return
     */
    List<Map<String,Object>> listadduser();

    /**
     * 添加信审信息
     * @param userIds
     */
    void addUserExamInfo(Long ... userIds) throws ManageException;
    /**
     *  查询复审字典
     * @param codeType
     * @return List<ExamineDictDetail>
     * @throws
     * @author Vic Tang
     * @date 2018/8/2 21:25
     * */
    List<ExamineDictDetail> selectBorrowExamineDictDetailInfo(String codeType);

    /**
     * 查询信审人员
     */
    List<UserExamine> listUserExamineInfo1();
    /**
     * 信审人员查询
     * @return
     */
    List<UserExamine> selectAllotMan();
    /**
     * 查询所有的字典信息
     */
    Page<ExamineDictDetail> selectExamineDictDetailInfo(Map<String, Object> params, Integer current, Integer pageSize);
    /**
     * 修改审批意见信息
     * @param id
     * @param realName
     * @param value
     * @author nmnl
     * @date 2017-12-28 21:42
     */
    int updateExamineDictDetail(Long id, String realName, String value);
    /**
     * 新增审批意见信息
     * @param exma
     * @author nmnl
     * @date 2017-12-28 21:42
     */
    int saveExamineDictDetail(ExamineDictDetail exma);
}
