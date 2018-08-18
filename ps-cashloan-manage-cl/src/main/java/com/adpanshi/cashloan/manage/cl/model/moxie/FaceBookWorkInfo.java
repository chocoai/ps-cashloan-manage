package com.adpanshi.cashloan.manage.cl.model.moxie;

/**
 * @author 8470
 * @description FaceBook工作信息
 * @create 2018-07-26 15:27
 **/

public class FaceBookWorkInfo {

    /**
     * 工作公司
     */
    private String company;

    /**
     * 工作职位
     */
    private String position;

    /**
     * 该公司起始时间
     */
    private String starttime;

    /**
     * 该公司离职时间
     */
    private String finishtime;

    /**
     * 工作城市
     */
    private String city;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
