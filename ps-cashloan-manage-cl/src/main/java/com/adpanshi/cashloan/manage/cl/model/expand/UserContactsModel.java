package com.adpanshi.cashloan.manage.cl.model.expand;


import com.adpanshi.cashloan.manage.cl.model.UserContacts;

import java.io.Serializable;

/**
 * 字典匹配的用户通讯录
 * Created by cc on 2017-09-05 15:11.
 */
public class UserContactsModel extends UserContacts implements Serializable,Comparable<UserContactsModel>{

    private Integer state ;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public int compareTo(UserContactsModel o) {
        return this.state.compareTo(o.getState());
    }
}
