package com.adpanshi.cashloan.manage.cl.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PayCheck implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_check.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_check.order_no
     *
     * @mbg.generated
     */
    private String orderNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_check.order_amount
     *
     * @mbg.generated
     */
    private BigDecimal orderAmount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_check.real_pay_amount
     *
     * @mbg.generated
     */
    private BigDecimal realPayAmount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_check.type
     *
     * @mbg.generated
     */
    private String type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_check.state
     *
     * @mbg.generated
     */
    private String state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_check.process_result
     *
     * @mbg.generated
     */
    private String processResult;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_check.process_way
     *
     * @mbg.generated
     */
    private String processWay;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_check.process_time
     *
     * @mbg.generated
     */
    private Date processTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_check.remark
     *
     * @mbg.generated
     */
    private String remark;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_pay_check.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cl_pay_check
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_check.id
     *
     * @return the value of cl_pay_check.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_check.id
     *
     * @param id the value for cl_pay_check.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_check.order_no
     *
     * @return the value of cl_pay_check.order_no
     *
     * @mbg.generated
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_check.order_no
     *
     * @param orderNo the value for cl_pay_check.order_no
     *
     * @mbg.generated
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_check.order_amount
     *
     * @return the value of cl_pay_check.order_amount
     *
     * @mbg.generated
     */
    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_check.order_amount
     *
     * @param orderAmount the value for cl_pay_check.order_amount
     *
     * @mbg.generated
     */
    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_check.real_pay_amount
     *
     * @return the value of cl_pay_check.real_pay_amount
     *
     * @mbg.generated
     */
    public BigDecimal getRealPayAmount() {
        return realPayAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_check.real_pay_amount
     *
     * @param realPayAmount the value for cl_pay_check.real_pay_amount
     *
     * @mbg.generated
     */
    public void setRealPayAmount(BigDecimal realPayAmount) {
        this.realPayAmount = realPayAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_check.type
     *
     * @return the value of cl_pay_check.type
     *
     * @mbg.generated
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_check.type
     *
     * @param type the value for cl_pay_check.type
     *
     * @mbg.generated
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_check.state
     *
     * @return the value of cl_pay_check.state
     *
     * @mbg.generated
     */
    public String getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_check.state
     *
     * @param state the value for cl_pay_check.state
     *
     * @mbg.generated
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_check.process_result
     *
     * @return the value of cl_pay_check.process_result
     *
     * @mbg.generated
     */
    public String getProcessResult() {
        return processResult;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_check.process_result
     *
     * @param processResult the value for cl_pay_check.process_result
     *
     * @mbg.generated
     */
    public void setProcessResult(String processResult) {
        this.processResult = processResult == null ? null : processResult.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_check.process_way
     *
     * @return the value of cl_pay_check.process_way
     *
     * @mbg.generated
     */
    public String getProcessWay() {
        return processWay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_check.process_way
     *
     * @param processWay the value for cl_pay_check.process_way
     *
     * @mbg.generated
     */
    public void setProcessWay(String processWay) {
        this.processWay = processWay == null ? null : processWay.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_check.process_time
     *
     * @return the value of cl_pay_check.process_time
     *
     * @mbg.generated
     */
    public Date getProcessTime() {
        return processTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_check.process_time
     *
     * @param processTime the value for cl_pay_check.process_time
     *
     * @mbg.generated
     */
    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_check.remark
     *
     * @return the value of cl_pay_check.remark
     *
     * @mbg.generated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_check.remark
     *
     * @param remark the value for cl_pay_check.remark
     *
     * @mbg.generated
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_pay_check.create_time
     *
     * @return the value of cl_pay_check.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_pay_check.create_time
     *
     * @param createTime the value for cl_pay_check.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_pay_check
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
        PayCheck other = (PayCheck) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
            && (this.getOrderAmount() == null ? other.getOrderAmount() == null : this.getOrderAmount().equals(other.getOrderAmount()))
            && (this.getRealPayAmount() == null ? other.getRealPayAmount() == null : this.getRealPayAmount().equals(other.getRealPayAmount()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getProcessResult() == null ? other.getProcessResult() == null : this.getProcessResult().equals(other.getProcessResult()))
            && (this.getProcessWay() == null ? other.getProcessWay() == null : this.getProcessWay().equals(other.getProcessWay()))
            && (this.getProcessTime() == null ? other.getProcessTime() == null : this.getProcessTime().equals(other.getProcessTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_pay_check
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        result = prime * result + ((getOrderAmount() == null) ? 0 : getOrderAmount().hashCode());
        result = prime * result + ((getRealPayAmount() == null) ? 0 : getRealPayAmount().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getProcessResult() == null) ? 0 : getProcessResult().hashCode());
        result = prime * result + ((getProcessWay() == null) ? 0 : getProcessWay().hashCode());
        result = prime * result + ((getProcessTime() == null) ? 0 : getProcessTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }
}