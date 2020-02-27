package com.wu.blog.common.aop;

import com.wu.blog.system.service.ShiroService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 更新过滤器
 */
@Aspect
@Component
public class RefreshFilterChainAspect {

    @Resource
    private ShiroService shiroService;

    @Pointcut("@annotation(com.wu.blog.common.annotation.RefreshFilterChain)")
    public void updateFilterChain(){}

    @AfterReturning("updateFilterChain()")
    public void doAfter(){shiroService.updateFilterChain();}

}
