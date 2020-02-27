package com.wu.blog.common.interceptor;

import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import sun.misc.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestLogHandlerInterceptor implements HandlerInterceptor {

    private static final Logger log= LoggerFactory.getLogger(RequestLogHandlerInterceptor.class);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler){
        if(log.isDebugEnabled()){
            log.debug("请求 URL:"+request.getRequestURL());
            log.debug("请求参数:"+ JSONUtil.toJsonStr(request.getParameterMap()));
        }
        return true;
    }
}
