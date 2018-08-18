package com.adpanshi.cashloan.manage.cl.model.expand;

import com.adpanshi.cashloan.manage.cl.model.UserApps;

/**
 * @author Vic Tang
 * @Description: 用户应用扩展类
 * @date 2018/8/10 14:40
 */
public class UserAppsModel extends UserApps implements Comparable<UserAppsModel>{
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public int compareTo(UserAppsModel o) {
        return this.status.compareTo(o.status);
    }
}
