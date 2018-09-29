package com.adpanshi.cashloan.manage.interceptor;

import com.adpanshi.cashloan.common.utils.JSONUtils;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fish_coder
 * @Title: DubboServiceFilter
 * @ProjectName fenqidai-dubbo
 * @Description: TODO
 * @date 2018/8/2216:32
 */
@Activate(group = { Constants.CONSUMER })
public class DubboServiceFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DubboServiceFilter.class);
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = null;
        Long takeTime = 0L;
        try{
            Long startTime = System.currentTimeMillis();
            result = invoker.invoke(invocation);
            if (result.getException() instanceof Exception)
            {
                throw new Exception(result.getException());
            }
            takeTime = System.currentTimeMillis() - startTime;
        }catch (Exception e)
        {
            LOGGER.error("Exception:{},request{},curr error:{},msg:{}", invocation.getClass(),
                    invocation.getArguments(), e.toString(), ExceptionUtils.getRootCause(e));
            result = new RpcResult(e);
            return result;
        } finally {
            if(takeTime>200){
                LOGGER.info("method:[{}],request:{},response:{},takeTime:{} ms",
                        invocation.getMethodName(), invocation.getArguments(), JSONUtils.toJSON(result),
                        takeTime);
            }
        }
        return result;
    }
}
