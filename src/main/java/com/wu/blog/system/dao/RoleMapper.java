package com.wu.blog.system.dao;

import com.wu.blog.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {

    List<Role> selectAllByQuery(Role roleQuery);

    int insert(Role role);

    int update(Role role);

    int deleteById(Integer roleId);

    Role selectById(Integer roleId);

    List<Role> selectAll();

    int count();
}