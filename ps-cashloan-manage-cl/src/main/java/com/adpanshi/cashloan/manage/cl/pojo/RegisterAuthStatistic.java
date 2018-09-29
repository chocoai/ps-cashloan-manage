package com.adpanshi.cashloan.manage.cl.pojo;

/**
 * @author Vic Tang
 * @Description: 用户注册和认证统计
 * @date 2018/8/22 20:57
 */
public class RegisterAuthStatistic {
    /**
     * 统计日期
     */
    private String date;
    /**
     * 当日注册人数
     */
    private Integer registerCount;
    /**
     * 当日身份认证通过人数
     */
    private Integer idCount;
    /**
     * 当日银行卡认证通过人数
     */
    private Integer bankCount;
    /**
     * 当日紧急联系人通过人数
     */
    private Integer contactCount;
    /**
     * 当日所有认证通过人数
     */
    private Integer totalCount;
    /**
     *当日身份认证通过比例
     */
    private Float idRatio;
    /**
     *当日银行卡认证通过比例
     */
    private Float bankRatio;
    /**
     *当日紧急联系人认证通过比例
     */
    private Float contactRatio;
    /**
     *当日所有认证通过比例
     */
    private Float totalRatio;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(Integer registerCount) {
        this.registerCount = registerCount;
    }

    public Integer getIdCount() {
        return idCount;
    }

    public void setIdCount(Integer idCount) {
        this.idCount = idCount;
    }

    public Integer getBankCount() {
        return bankCount;
    }

    public void setBankCount(Integer bankCount) {
        this.bankCount = bankCount;
    }

    public Integer getContactCount() {
        return contactCount;
    }

    public void setContactCount(Integer contactCount) {
        this.contactCount = contactCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Float getIdRatio() {
        return (float)idCount/registerCount*100;
    }

    public Float getBankRatio() {
        return (float)bankCount/registerCount*100;
    }

    public Float getContactRatio() {
        return (float)contactCount/registerCount*100;
    }

    public Float getTotalRatio() {
        return (float)totalCount/registerCount*100;
    }
}
