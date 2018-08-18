package com.adpanshi.cashloan.manage.cl.model.moxie;

/**
 * @author 8470
 * @description FaceBook历史登录信息
 * @create 2018-07-26 15:37
 **/

public class FaceBookHistoricalLoginInfo {

    /**
     * 登录方式
     */
    private String way;

    /**
     * 登录时间
     */
    private String time;

    /**
     * 页面所示登录时间
     */
    private String pagetime;

    /**
     * 登录地点
     */
    private String locus;

    /**
     * 登录工具
     */
    private String tool;

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPagetime() {
        return pagetime;
    }

    public void setPagetime(String pagetime) {
        this.pagetime = pagetime;
    }

    public String getLocus() {
        return locus;
    }

    public void setLocus(String locus) {
        this.locus = locus;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }
}
