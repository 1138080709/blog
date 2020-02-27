package com.wu.blog.common.shiro.credential;

import com.wu.blog.common.shiro.BlogProperties;
import com.wu.blog.common.utils.IPUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MD5 密码匹配器
 *
 * 密码校验失败后计数，当超出 ${shiro-action.retry-count} 次后，禁止登录 ${shiro-action.retry-timeout} 秒.
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Resource
    private RedisTemplate<String, AtomicInteger> redisTemplate;

    @Resource
    private BlogProperties blogProperties;

    public RetryLimitHashedCredentialsMatcher(String hashAlgorithmName) {
        super(hashAlgorithmName);
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        ValueOperations<String,AtomicInteger> opsForValue=redisTemplate.opsForValue();
        String username=(String)token.getPrincipal();
        String key=username+ IPUtils.getIpAddr();

        // 超级管理员不进行登录次数校验
        if(!blogProperties.getSuperAdminUsername().equals(key)) {
            AtomicInteger retryCount=opsForValue.get(key);
            if(retryCount == null) {
                retryCount=new AtomicInteger(0);
            }

            if(retryCount.incrementAndGet()> blogProperties.getRetryCount()){
                throw new ExcessiveAttemptsException();
            }

            Integer retryTimeout= blogProperties.getRetryTimeout()==null?300: blogProperties.getRetryTimeout();
            opsForValue.set(key,retryCount,retryTimeout, TimeUnit.SECONDS);
        }

        boolean matches=super.doCredentialsMatch(token, info);
        if(matches){
            redisTemplate.delete(key);
        }

        return matches;
    }
}
