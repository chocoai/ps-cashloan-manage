package com.adpanshi.cashloan.manage.aop;

import com.adpanshi.cashloan.manage.arc.mapper.ArcLogMapper;
import com.adpanshi.cashloan.manage.arc.model.ArcLogWithBLOBs;
import com.adpanshi.cashloan.manage.arc.model.SysUser;
import com.adpanshi.cashloan.manage.core.common.token.TokenManager;
import com.alibaba.fastjson.JSON;

import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class LogAspect {
    @Autowired
    private ArcLogMapper logMapper;
    @Autowired
    private TokenManager manager;

    /**
     *
     * @Description: 方法调用成功后触发   记录结束时间
     * @author tq
     */
    public  void after(JoinPoint joinPoint, Object rtv) throws Exception {
        //获取类名
        String targetName = joinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取入参
        Object[] arguments = joinPoint.getArgs();
        if(methodName.equals("setReqAndRes")){
            return;
        }
        String  url = null;
        String userId = null;
        String loginName = null;
        String ip = null;
        String input = "";
        //获取request
        if((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()!=null){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            url = request.getRequestURL().toString();
            input = JSON.toJSONString(request.getParameterMap()).replace("[","").replace("]","")
                    .replaceAll("\\\\","");
            ip = request.getHeader("x-forwarded-for");
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            SysUser sysUser = manager.getSysUser(manager.getToken(request));
            if(sysUser!=null) {
                userId = String.valueOf(sysUser.getId());
                loginName = sysUser.getUserName();
            }

        }
        String result = "";
        if(rtv!=null){
            result = JSON.toJSONString(rtv).replace("[","").replace("]","")
                    .replaceAll("\\\\","");
        }
        ArcLogWithBLOBs log = new ArcLogWithBLOBs();
        log.setCreateTime(new Date());
        log.setInputParam(input);
        log.setLoginName(loginName);
        log.setOutputParam(result);
        log.setOptionMethod(url);
        log.setUserId(userId!=null?Long.valueOf(userId):null);
        log.setOptionIp(ip);
        logMapper.insertSelective(log);
    }


}
