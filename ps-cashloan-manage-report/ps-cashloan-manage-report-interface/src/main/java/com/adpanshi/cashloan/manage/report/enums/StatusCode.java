package com.adpanshi.cashloan.manage.report.enums;


/**
 * 状态枚举
 */
public enum StatusCode {
    //通用
    SUCCESS("交互成功", 10000),
    PARAMS_WRONG("非法传参", 10020),
    OTHER_ERROR("其它错误", 10099),
    NO_REPORT("未获取到信用报告",10030)
    ;

    private String content;
    private Integer value;

    private StatusCode(String content, Integer value) {
        this.content = content;
        this.value = value;
    }

    public static StatusCode valueOf(Integer value) {
        StatusCode[] entities = StatusCode.values();
        for (StatusCode entity : entities) {
            if (entity.getValue().equals(value)) {
                return entity;
            }
        }
        return null;
    }

    public String getContent() {
        return this.content;
    }

    public Integer getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return content;
    }
}
