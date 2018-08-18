package com.adpanshi.cashloan.manage.cl.mapper;

import com.adpanshi.cashloan.common.mapper.RDBatisDao;
import com.adpanshi.cashloan.manage.cl.model.Sms;
import com.adpanshi.cashloan.manage.cl.model.SmsExample;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
@RDBatisDao
public interface SmsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_sms
     *
     * @mbg.generated
     */
    long countByExample(SmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_sms
     *
     * @mbg.generated
     */
    int deleteByExample(SmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_sms
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_sms
     *
     * @mbg.generated
     */
    int insert(Sms record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_sms
     *
     * @mbg.generated
     */
    int insertSelective(Sms record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_sms
     *
     * @mbg.generated
     */
    List<Sms> selectByExample(SmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_sms
     *
     * @mbg.generated
     */
    Sms selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_sms
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") Sms record, @Param("example") SmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_sms
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") Sms record, @Param("example") SmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_sms
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Sms record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_sms
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Sms record);

    List<Sms> findResendList(Map<String, Object> map);

    int updateByIds(Map<String, Object> smsInfo);

    void updateByMsgids(Map<String, Object> tyhSmsInfo);
    /**
     * 查询某号码某种类型当天已发送次数
     * @param data
     * @return
     */
    int countDayTime(Map<String, Object> data);
}