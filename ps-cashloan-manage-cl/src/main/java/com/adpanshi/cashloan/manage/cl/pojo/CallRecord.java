package com.adpanshi.cashloan.manage.cl.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Vic Tang
 * @Description: 用户通话记录
 * @date 2018/8/13 11:28
 */
public class CallRecord implements Serializable,Comparable<CallRecord> {

    /**
     * 通话时间
     */
    Date date;
    /**
     * 通话时长（秒）
     */
    Integer duration;
    /**
     *通话人姓名
     */
    String name;
    /**
     * 号码地区
     */
    String location;
    /**
     * 通话号码
     */
    String matchedNumber;
    /**
     * 通话类型
     */
    String type;

    /**
     * 匹配状态
     */
    private Integer status;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMatchedNumber() {
        return matchedNumber;
    }

    public void setMatchedNumber(String matchedNumber) {
        this.matchedNumber = matchedNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(CallRecord o) {
        return this.status.compareTo(o.status);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
