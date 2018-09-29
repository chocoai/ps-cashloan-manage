package com.adpanshi.cashloan.manage.cl.pojo;

/**
 * @author Vic Tang
 * @Description: 注册统计entity
 * @date 2018/8/22 17:19
 */
public class RegisterStatistic {
    /**
     * 年龄分布(20~25,26~30,31~35,36~40,未知)
     */
    private String age;
    /**
     * 性别分布(男,女,未知)
     */
    private String gender;
    /**
     * 统计人数
     */
    private Integer value;

    /**
     * 统计属性
     */
    private String name;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
