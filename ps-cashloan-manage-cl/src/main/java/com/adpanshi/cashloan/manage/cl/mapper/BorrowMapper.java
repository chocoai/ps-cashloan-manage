package com.adpanshi.cashloan.manage.cl.mapper;


import com.adpanshi.cashloan.common.mapper.RDBatisDao;
import com.adpanshi.cashloan.manage.cl.model.Borrow;
import com.adpanshi.cashloan.manage.cl.model.BorrowExample;
import java.util.List;
import java.util.Map;

import com.adpanshi.cashloan.manage.cl.model.expand.BorrowModel;
import org.apache.ibatis.annotations.Param;
@RDBatisDao
public interface BorrowMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_borrow
     *
     * @mbg.generated
     */
    long countByExample(BorrowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_borrow
     *
     * @mbg.generated
     */
    int deleteByExample(BorrowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_borrow
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_borrow
     *
     * @mbg.generated
     */
    int insert(Borrow record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_borrow
     *
     * @mbg.generated
     */
    int insertSelective(Borrow record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_borrow
     *
     * @mbg.generated
     */
    List<Borrow> selectByExample(BorrowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_borrow
     *
     * @mbg.generated
     */
    Borrow selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_borrow
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") Borrow record, @Param("example") BorrowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_borrow
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") Borrow record, @Param("example") BorrowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_borrow
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Borrow record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_borrow
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Borrow record);
    /**
     * 借款部分还款信息
     * @param map
     * @return
     */
    List<BorrowModel> listBorrowModel(Map<String, Object> map);
    /**
     * 批量保存
     */
    int saveAll(@Param("list") List<Borrow> borrowList);

    List<Borrow> listSelective(Map<String, Object> map);

    BorrowModel getModelByBorrowId(Long borrowId);
}