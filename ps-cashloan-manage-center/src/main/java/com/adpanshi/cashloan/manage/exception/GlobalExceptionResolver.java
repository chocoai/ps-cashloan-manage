package com.adpanshi.cashloan.manage.exception;

import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.common.enums.ManageExceptionEnum;
import com.adpanshi.cashloan.common.exception.ManageException;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther huangqin
 * @data 2017-12-18
 * @dsscription Spring全局异常
 * @since 2017-12-18 21:12:47
 * @version 1.0.0
 * */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

	private static Logger LOG = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {
        Map<String, Object> attributes = new HashMap<>();
        //返回的ModelAndView
		ModelAndView mv = new ModelAndView();
        //使用FastJson提供的FastJsonJsonView视图返回，不需要捕获异常
        FastJsonJsonView view = new FastJsonJsonView();
        if(ex instanceof ManageException){
        	/** manage自定义异常   **/
        	LOG.info("---------异常捕获 ManageLogInException------:"+ ex.getMessage(),ex);
            attributes.put(Constant.RESPONSE_CODE,((ManageException) ex).getErrorCode());
            attributes.put(Constant.RESPONSE_CODE_MSG, ((ManageException) ex).getErrorMsg());
		}else if(ex instanceof RuntimeException){
            /** 运行时异常  **/
            LOG.info("---------异常捕获 RuntimeException------:"+ ex.getMessage(),ex);
            attributes.put(Constant.RESPONSE_CODE, ManageExceptionEnum.FAIL_CODE_VALUE.Code());
            attributes.put(Constant.RESPONSE_CODE_MSG, ManageExceptionEnum.FAIL_CODE_VALUE.Msg());
        }else{
			/** 其他系统错误  **/
			LOG.info("---------异常捕获 其他系统异常------"+ ex.getMessage(),ex);
			//抛出系统异常
            attributes.put(Constant.RESPONSE_CODE, ManageExceptionEnum.OTHER_CODE_VALUE.Code());
            attributes.put(Constant.RESPONSE_CODE_MSG, ManageExceptionEnum.OTHER_CODE_VALUE.Msg());
		}
        view.setAttributesMap(attributes);
        mv.setView(view);
        return mv;
	}
}
