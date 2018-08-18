package com.adpanshi.cashloan.manage.cl.model.moxie;

/**
 * @author 8470
 * @description FaceBook支付信息
 * @create 2018-07-26 15:31
 **/

public class FaceBookPaymentsInfo {

    /**
     * 支付日期
     */
    private String date;

    /**
     * 支付人姓名
     */
    private String name;

    /**
     * 支付状态
     */
    private String status;

    /**
     * 收取金额
     */
    private String received;

    /**
     * 支付金额
     */
    private String paid;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }
}
