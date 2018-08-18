package com.adpanshi.cashloan.manage.cl.model.moxie;

/**
 * @author 8470
 * @description FaceBook教育信息
 * @create 2018-07-26 16:06
 **/

public class FaceBookEducationInfo {

    /**
     * 大学学校名称
     */
    private String collegename;
    /**
     * 大学毕业时间
     */
    private String collegegraduation;

    /**
     * 高中学校名称
     */
    private String highschoolname;
    /**
     * 高中毕业时间
     */
    private String highschoolgraduation;


    public String getCollegename() {
        return collegename;
    }

    public void setCollegename(String collegename) {
        this.collegename = collegename;
    }

    public String getCollegegraduation() {
        return collegegraduation;
    }

    public void setCollegegraduation(String collegegraduation) {
        this.collegegraduation = collegegraduation;
    }

    public String getHighschoolname() {
        return highschoolname;
    }

    public void setHighschoolname(String highschoolname) {
        this.highschoolname = highschoolname;
    }

    public String getHighschoolgraduation() {
        return highschoolgraduation;
    }

    public void setHighschoolgraduation(String highschoolgraduation) {
        this.highschoolgraduation = highschoolgraduation;
    }
}
