package com.adpanshi.cashloan.manage.cl.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Vic Tang
 * @Description: 通话记录号码次数
 * @date 2018/8/21 16:01
 */
public class CallRecordCount implements Serializable {
    /**
     * 通话手机号
     */
    private String phoneNumber;
    /**
     * 通话次数
     */
    private Integer count;
    /**
     * 最早通话时间
     */
    private Date startTime;
    /**
     * 最近通话时间
     */
    private Date endTime;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
