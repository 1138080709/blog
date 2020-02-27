package com.wu.blog.common.shiro.realm;

import com.wu.blog.common.constants.AuthcTypeEnum;
import org.springframework.stereotype.Component;

/**
 * Gitgub OAuth2 Realm
 */
@Component
public class OAuth2GithubRealm extends OAuth2Realm{
    @Override
    public AuthcTypeEnum getAuthcTypeEnum() {
        return AuthcTypeEnum.GITHUB;
    }
}
