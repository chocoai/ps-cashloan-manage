package com.adpanshi.cashloan.manage.cl.service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Vic Tang
 * @Description: 附件service
 * @date 2018/8/4 0:31
 */
public interface SysAttachmentService {
    /**
     *  获取图片
     * @method: getImg
     * @param request
     * @param response
     * @return: javax.servlet.http.HttpServletResponse
     * @throws
     * @Author: Mr.Wange
     * @Date: 2018/7/25 14:01
     */
    HttpServletResponse getImg(HttpServletRequest request, HttpServletResponse response);
}
