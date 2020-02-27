package com.wu.blog.common.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 在Shiro使用多Realm时，对于Realm中抛出的异常，他都会进行捕获，然后输出日志.
 * 但是我们系统有统一异常处理，所以不需要他捕获我们自定义的异常，这里将异常抛出.
 */
public class EnhanceModularRealmAuthenticator extends ModularRealmAuthenticator {

    private static final Logger log= LoggerFactory.getLogger(EnhanceModularRealmAuthenticator.class);

    /**
     * 抛出realm中遇到的第一个异常
     */
    @Override
    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
        AuthenticationStrategy strategy=getAuthenticationStrategy();
        AuthenticationInfo aggregate=strategy.beforeAllAttempts(realms,token);

        if(log.isTraceEnabled()){
            log.trace("Iterating through {} realms for PAM authentication", realms.size());
        }

        for(Realm realm:realms){
            aggregate=strategy.beforeAttempt(realm,token,aggregate);

            if(realm.supports(token)) {
                log.trace("Attempting to authenticate token [{}] using realm [{}]", token, realm);

                AuthenticationInfo info;
                // 有异常从此处抛出
                info=realm.getAuthenticationInfo(token);

                aggregate=strategy.afterAttempt(realm,token,info,aggregate,null);
            }else{
                log.debug("Realm [{}] does not support token {}.  Skipping realm.", realm, token);
            }
        }
        aggregate=strategy.afterAllAttempts(token,aggregate);
        return aggregate;
    }
}
