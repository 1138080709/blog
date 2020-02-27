package com.wu.blog.system.dao;

import com.wu.blog.system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {

    int insert(User user);

    User selectByPrimaryKey(@Param("userId") Integer userId);

    /**
     * 获取用户所拥有的所有角色
     */
    Set<String> selectRoleNameByUsername(@Param("username") String username);

    /**
     * 获取用户所拥有的所有权限
     */
    Set<String> selectPermsByUsername(@Param("username") String username);

    /**
     * 根据用户名获得用户
     */
    User selectOneByUsername(String username);

    /**
     * 统计已经有几个此用户名, 用来检测用户名是否重复.
     */
    int countByUserName(String username);

    void updateLastLoginTimeByUsername(String username);

    /**
     * 根据邮箱激活码, 查询要被激活的用户.
     */
    User selectByActiveCode(@Param("activeCode") String token);

    int activeUserByUserId(Integer userId);

    /**
     * 根据查询信息查询用户
     */
    List<User> selectAllWithInfo(User userQuery);

    /**
     * 查询此用户拥有的所有角色的 ID
     *
     * @param userId 用户 ID
     * @return 拥有的角色 ID 数组
     */
    Integer[] selectRoleIdsByUserId(@Param("userId") Integer userId);

    /**
     * 统计已经有几个此用户名, 用来检测用户名是否重复 (不包含某用户 ID).
     */
    int countByUserNameNotIncludeUserId(@Param("username") String username, @Param("userId")Integer userId);

    int update(User user);

    int updateStatusById(@Param("userId") Integer userId, @Param("status") Integer status);

    int delete(Integer userId);

    int updatePasswordById(@Param("userId") Integer userId, @Param("password") String encryptPassword, @Param("salt") String salt);

    int count();

    /**
     * 获取用户所拥有的操作权限
     */
    Set<String> selectOperatorPermsByUsername(@Param("username") String username);
}