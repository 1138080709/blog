package com.wu.blog.system.dao;

import com.wu.blog.system.entity.UserAuths;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAuthsMapper {
    UserAuths selectOneByIdentityTypeAndUserId(@Param("identityType") String identityType, @Param("userId") Integer userId);

    UserAuths selectOneByIdentityTypeAndIdentifier(@Param("identityType") String identityType, @Param("username") String username);

    int insert(UserAuths record);

    int delete(Integer id);
}