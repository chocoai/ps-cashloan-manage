package com.adpanshi.cashloan.manage.arc.mapper;

import com.adpanshi.cashloan.common.mapper.RDBatisDao;
import com.adpanshi.cashloan.manage.arc.model.CreditLog;
import com.adpanshi.cashloan.manage.arc.model.CreditLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
@RDBatisDao
public interface CreditLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table arc_credit_log
     *
     * @mbg.generated
     */
    long countByExample(CreditLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table arc_credit_log
     *
     * @mbg.generated
     */
    int deleteByExample(CreditLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table arc_credit_log
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table arc_credit_log
     *
     * @mbg.generated
     */
    int insert(CreditLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table arc_credit_log
     *
     * @mbg.generated
     */
    int insertSelective(CreditLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table arc_credit_log
     *
     * @mbg.generated
     */
    List<CreditLog> selectByExample(CreditLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table arc_credit_log
     *
     * @mbg.generated
     */
    CreditLog selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table arc_credit_log
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") CreditLog record, @Param("example") CreditLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table arc_credit_log
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") CreditLog record, @Param("example") CreditLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table arc_credit_log
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CreditLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table arc_credit_log
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CreditLog record);
}