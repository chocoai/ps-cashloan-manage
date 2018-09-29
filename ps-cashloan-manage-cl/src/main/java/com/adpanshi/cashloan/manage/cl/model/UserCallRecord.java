package com.adpanshi.cashloan.manage.cl.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户通话记录实体
 * 
 * @author Vic Tang
 * @version 1.0.0
 * @date 2018-08-15 15:07:31

 *
 *
 */
 public class UserCallRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键Id
    */
    private Long id;

    /**
    * 通话记录
    */
    private Object info;

    /**
    * 用户表主键
    */
    private Long userId;

    /**
    * 创建时间
    */
    private Date createTime;


    /**
    * 获取主键Id
    *
    * @return id
    */
    public Long getId(){
        return id;
    }

    /**
    * 设置主键Id
    * 
    * @param id
    */
    public void setId(Long id){
        this.id = id;
    }

    /**
    * 获取通话记录
    *
    * @return 通话记录
    */
    public Object getInfo(){
        return info;
    }

    /**
    * 设置通话记录
    * 
    * @param info 要设置的通话记录
    */
    public void setInfo(Object info){
        this.info = info;
    }

    /**
    * 获取用户表主键
    *
    * @return 用户表主键
    */
    public Long getUserId(){
        return userId;
    }

    /**
    * 设置用户表主键
    * 
    * @param userId 要设置的用户表主键
    */
    public void setUserId(Long userId){
        this.userId = userId;
    }

    /**
    * 获取创建时间
    *
    * @return 创建时间
    */
    public Date getCreateTime(){
        return createTime;
    }

    /**
    * 设置创建时间
    * 
    * @param createTime 要设置的创建时间
    */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

}
