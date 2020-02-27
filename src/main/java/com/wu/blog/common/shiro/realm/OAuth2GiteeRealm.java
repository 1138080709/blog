package com.wu.blog.common.shiro.realm;

import com.wu.blog.common.constants.AuthcTypeEnum;
import org.springframework.stereotype.Component;

/**
 * Gitee OAuth2 Realm
 */
@Component
public class OAuth2GiteeRealm extends OAuth2Realm{
    @Override
    public AuthcTypeEnum getAuthcTypeEnum() {
        return AuthcTypeEnum.GITEE;
    }
}
