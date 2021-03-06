package com.adpanshi.cashloan.manage.cl.model;

import java.io.Serializable;
import java.util.Date;

public class BorrowProgress implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_borrow_progress.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_borrow_progress.user_id
     *
     * @mbg.generated
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_borrow_progress.borrow_id
     *
     * @mbg.generated
     */
    private Long borrowId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_borrow_progress.state
     *
     * @mbg.generated
     */
    private String state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_borrow_progress.remark
     *
     * @mbg.generated
     */
    private String remark;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_borrow_progress.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_borrow_progress.loan_time
     *
     * @mbg.generated
     */
    private Date loanTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_borrow_progress.repay_time
     *
     * @mbg.generated
     */
    private Date repayTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cl_borrow_progress
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_borrow_progress.id
     *
     * @return the value of cl_borrow_progress.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_borrow_progress.id
     *
     * @param id the value for cl_borrow_progress.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_borrow_progress.user_id
     *
     * @return the value of cl_borrow_progress.user_id
     *
     * @mbg.generated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_borrow_progress.user_id
     *
     * @param userId the value for cl_borrow_progress.user_id
     *
     * @mbg.generated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_borrow_progress.borrow_id
     *
     * @return the value of cl_borrow_progress.borrow_id
     *
     * @mbg.generated
     */
    public Long getBorrowId() {
        return borrowId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_borrow_progress.borrow_id
     *
     * @param borrowId the value for cl_borrow_progress.borrow_id
     *
     * @mbg.generated
     */
    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_borrow_progress.state
     *
     * @return the value of cl_borrow_progress.state
     *
     * @mbg.generated
     */
    public String getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_borrow_progress.state
     *
     * @param state the value for cl_borrow_progress.state
     *
     * @mbg.generated
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_borrow_progress.remark
     *
     * @return the value of cl_borrow_progress.remark
     *
     * @mbg.generated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_borrow_progress.remark
     *
     * @param remark the value for cl_borrow_progress.remark
     *
     * @mbg.generated
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_borrow_progress.create_time
     *
     * @return the value of cl_borrow_progress.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_borrow_progress.create_time
     *
     * @param createTime the value for cl_borrow_progress.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_borrow_progress.loan_time
     *
     * @return the value of cl_borrow_progress.loan_time
     *
     * @mbg.generated
     */
    public Date getLoanTime() {
        return loanTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_borrow_progress.loan_time
     *
     * @param loanTime the value for cl_borrow_progress.loan_time
     *
     * @mbg.generated
     */
    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cl_borrow_progress.repay_time
     *
     * @return the value of cl_borrow_progress.repay_time
     *
     * @mbg.generated
     */
    public Date getRepayTime() {
        return repayTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cl_borrow_progress.repay_time
     *
     * @param repayTime the value for cl_borrow_progress.repay_time
     *
     * @mbg.generated
     */
    public void setRepayTime(Date repayTime) {
        this.repayTime = repayTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_borrow_progress
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
        BorrowProgress other = (BorrowProgress) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getBorrowId() == null ? other.getBorrowId() == null : this.getBorrowId().equals(other.getBorrowId()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getLoanTime() == null ? other.getLoanTime() == null : this.getLoanTime().equals(other.getLoanTime()))
            && (this.getRepayTime() == null ? other.getRepayTime() == null : this.getRepayTime().equals(other.getRepayTime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cl_borrow_progress
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getBorrowId() == null) ? 0 : getBorrowId().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getLoanTime() == null) ? 0 : getLoanTime().hashCode());
        result = prime * result + ((getRepayTime() == null) ? 0 : getRepayTime().hashCode());
        return result;
    }
}