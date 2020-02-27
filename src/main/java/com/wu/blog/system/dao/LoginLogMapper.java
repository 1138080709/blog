package com.wu.blog.system.dao;

import com.wu.blog.system.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoginLogMapper {
    void insert(LoginLog loginLog);

    List<LoginLog> selectAll();

    int count();

    List<Integer> recentlyWeekLoginCount(String username);
}
