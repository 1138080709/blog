package com.wu.blog.system.service;


import com.wu.blog.common.constants.AuthcTypeEnum;
import com.wu.blog.system.dao.UserAuthsMapper;
import com.wu.blog.system.entity.UserAuths;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserAuthsService {

    @Resource
    private UserAuthsMapper userAuthsDao;

    public UserAuths selectOneByIdentityTypeAndUserId(AuthcTypeEnum authcTypeEnum, Integer userId) {
        return userAuthsDao.selectOneByIdentityTypeAndUserId(authcTypeEnum.getDescription(),userId);
    }

    public UserAuths selectOneByIdentityTypeAndIdentifier(AuthcTypeEnum authcTypeEnum, String username) {
        return userAuthsDao.selectOneByIdentityTypeAndIdentifier(authcTypeEnum.getDescription(),username);
    }

    public int insert(UserAuths record) {
        return userAuthsDao.insert(record);
    }

    public int delete(Integer id) {
        return userAuthsDao.delete(id);
    }
}
