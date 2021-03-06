package com.adpanshi.cashloan.manage.cl.mapper;

import com.adpanshi.cashloan.common.mapper.RDBatisDao;
import com.adpanshi.cashloan.manage.cl.model.UserExamine;
import com.adpanshi.cashloan.manage.cl.model.UserExamineExample;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
@RDBatisDao
public interface UserExamineMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_user_examine
     *
     * @mbg.generated
     */
    long countByExample(UserExamineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_user_examine
     *
     * @mbg.generated
     */
    int deleteByExample(UserExamineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_user_examine
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_user_examine
     *
     * @mbg.generated
     */
    int insert(UserExamine record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_user_examine
     *
     * @mbg.generated
     */
    int insertSelective(UserExamine record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_user_examine
     *
     * @mbg.generated
     */
    List<UserExamine> selectByExample(UserExamineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_user_examine
     *
     * @mbg.generated
     */
    UserExamine selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_user_examine
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") UserExamine record, @Param("example") UserExamineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_user_examine
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") UserExamine record, @Param("example") UserExamineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_user_examine
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(UserExamine record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_user_examine
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(UserExamine record);

    /**
     * 查询全部信审人信息
     * @param userlist
     * @return
     */
    List<UserExamine> listUserExamineInfo(Map<String,Object> userlist);
    /**
     * 查询未添加的信审人信息
     * @return
     */
    List<Map<String,Object>> listadduser();
}