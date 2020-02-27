package com.wu.blog.system.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    int deleteUserRoleByUserId(Integer userId);

    /**
     * 插入多条 用户色-角色 关联关系
     */
    int insertList(@Param("userId") Integer userId, @Param("roleIds")Integer[] roleIds);

    List<Integer> selectUserIdByRoleId(@Param("roleId") Integer roleId);
}
