package com.wu.blog.common.utils;

import com.wu.blog.system.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.stereotype.Component;

@Component
public class ShiroUtils {

    public static final String USER_LOCK="0";

    /**
     * 获取当前登录用户
     */
    public static User getCurrentUser(){
        User user= (User)SecurityUtils.getSubject().getPrincipal();
        if(user==null){
            throw new UnauthenticatedException("未登录被拦截");
        }
        return user;
    }
}
