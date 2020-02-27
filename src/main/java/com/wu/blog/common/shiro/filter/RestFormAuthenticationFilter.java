package com.wu.blog.common.shiro.filter;

import com.wu.blog.common.utils.IPUtils;
import com.wu.blog.common.utils.ResultBean;
import com.wu.blog.common.utils.WebHelper;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.session.HttpServletSession;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 修改后的authc过滤器，添加对AJAX请求的支持
 */
public class RestFormAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger log= LoggerFactory.getLogger(RestFormAuthenticationFilter.class);

    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
        boolean flag;
        String requestURI=this.getPathWithinApplication(request);

        String[] strings= path.split("==");

        if(strings.length<=1){
            // 普通的URL，正常处理
            flag=this.pathsMatch(strings[0],requestURI);
        }else{
            // 获取当前请求的http method.
            String httpMethod= WebUtils.toHttp(request).getMethod().toUpperCase();
            // 匹配当前请求的url和http method 与过滤器链中的是否一致
            flag=httpMethod.equals(strings[1].toUpperCase())&&this.pathsMatch(strings[0],requestURI);
        }

        if (flag) {
            log.debug("URL : [{}] matching authc filter : [{}]", requestURI, path);
        }
        return flag;
    }


    /**
     * 当没有权限被拦截时：
     *              如果时AJAX请求,则返回JSON数据
     *              如果是普通请求，则跳转到配置UnauthorizedUrl页面
     *
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest=(HttpServletRequest)request;
        if(isLoginRequest(request,response)) {
            if(isLoginSubmission(request,response)){
                if(log.isTraceEnabled()){
                    log.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(request,response);
            }else {
                if(log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }
                // allow them to see the login page ;)
                return true;
            }
        }else {
            if(log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
                        "Authentication url [" + getLoginUrl() + "]");
            }

            if(WebHelper.isAjaxRequest(WebUtils.toHttp(request))){
                if(log.isTraceEnabled()){
                    log.debug("sessionId: [{}], ip: [{}] 请求 restful url : {}, 未登录被拦截.", httpServletRequest.getRequestedSessionId(), IPUtils.getIpAddr(),
                            this.getPathWithinApplication(request));
                }

                WebHelper.writeJson(ResultBean.error("未登录"),response);
            }else{
                saveRequestAndRedirectToLogin(request,response);
            }
            return false;
        }
    }


}
