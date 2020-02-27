package com.wu.blog.system.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleMenuMapper {
    int deleteByRoleId(@Param("roleId") Integer roleId);

    int insertRoleMenus(@Param("roleId") Integer roleId, @Param("menuIds") Integer[] menuIds);

    Integer[] getMenusByRoleId(@Param("roleId") Integer roleId);

    int deleteByMenuId(@Param("menuId") Integer menuId);
}