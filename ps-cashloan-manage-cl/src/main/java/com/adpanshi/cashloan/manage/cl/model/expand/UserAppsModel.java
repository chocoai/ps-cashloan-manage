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

    public static UserAppsModel parse(UserApps apps) {
        UserAppsModel model = new UserAppsModel();
        model.setUserId(apps.getUserId());
        model.setPackageName(apps.getPackageName());
        model.setAppName(apps.getAppName());
        model.setIexpress(apps.getIexpress());
        model.setSystemType(apps.getSystemType());
        model.setIexpress(apps.getIexpress());
        model.setGmtCreateTime(apps.getGmtCreateTime());
        model.setRemarks(apps.getRemarks());
        model.setId(apps.getId());
        model.setState(apps.getState());
        return model;
     }
}
