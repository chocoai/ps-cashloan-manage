package com.adpanshi.cashloan.manage.cl.model;

import java.io.Serializable;
import java.util.Date;

public class PayRespLog implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_resp_log.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_resp_log.order_no
     *
     * @mbg.generated
     */
    private String orderNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_resp_log.type
     *
     * @mbg.generated
     */
    private Boolean type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_resp_log.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_resp_log.params
     *
     * @mbg.generated
     */
    private String params;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cl_pay_resp_log
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_resp_log.id
     *
     * @return the value of cl_pay_resp_log.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_resp_log.id
     *
     * @param id the value for cl_pay_resp_log.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_resp_log.order_no
     *
     * @return the value of cl_pay_resp_log.order_no
     *
     * @mbg.generated
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_resp_log.order_no
     *
     * @param orderNo the value for cl_pay_resp_log.order_no
     *
     * @mbg.generated
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_resp_log.type
     *
     * @return the value of cl_pay_resp_log.type
     *
     * @mbg.generated
     */
    public Boolean getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_resp_log.type
     *
     * @param type the value for cl_pay_resp_log.type
     *
     * @mbg.generated
     */
    public void setType(Boolean type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_resp_log.create_time
     *
     * @return the value of cl_pay_resp_log.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_resp_log.create_time
     *
     * @param createTime the value for cl_pay_resp_log.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_resp_log.params
     *
     * @return the value of cl_pay_resp_log.params
     *
     * @mbg.generated
     */
    public String getParams() {
        return params;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_resp_log.params
     *
     * @param params the value for cl_pay_resp_log.params
     *
     * @mbg.generated
     */
    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_pay_resp_log
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        PayRespLog other = (PayRespLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getParams() == null ? other.getParams() == null : this.getParams().equals(other.getParams()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_pay_resp_log
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getParams() == null) ? 0 : getParams().hashCode());
        return result;
    }
}