package com.adpanshi.cashloan.manage.report.bo;

import com.adpanshi.cashloan.manage.report.enums.StatusCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 运行响应
 */
public class ManageReportResponseBo<T> implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int ret_code;

    private String ret_msg;

    private long time = new Date().getTime();

    private T data;

    public ManageReportResponseBo() {
    }

    public ManageReportResponseBo(StatusCode code) {
        this.ret_code = code.getValue();
        this.ret_msg = code.getContent();
    }

    public ManageReportResponseBo(StatusCode code, T data) {
        this.ret_code = code.getValue();
        this.ret_msg = code.getContent();
        this.data = data;
    }

    public ManageReportResponseBo(StatusCode code, String remark, T data) {
        this.ret_code = code.getValue();
        this.ret_msg = StringUtils.isEmpty(remark)?code.getContent():remark;
        this.data = data;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <R> ManageReportResponseBo<R> success() {
        return new ManageReportResponseBo<R>(StatusCode.SUCCESS);
    }

    public static <R> ManageReportResponseBo<R> success(R data) {
        return new ManageReportResponseBo<R>(StatusCode.SUCCESS, data);
    }

    public static <R> ManageReportResponseBo<R> success(R data, String remark) {
        return new ManageReportResponseBo<R>(StatusCode.SUCCESS, remark, data);
    }

    public static <R> ManageReportResponseBo<R> error(StatusCode code) {
        return new ManageReportResponseBo<R>(code, null);
    }

    public static <R> ManageReportResponseBo<R> error(StatusCode code, String remark) {
        return new ManageReportResponseBo<R>(code, remark, null);
    }

    @JsonIgnore
    private Integer nodeInstId;

    public Integer getNodeInstId() {
        return nodeInstId;
    }

    public void setNodeInstId(Integer nodeInstId) {
        this.nodeInstId = nodeInstId;
    }

}
