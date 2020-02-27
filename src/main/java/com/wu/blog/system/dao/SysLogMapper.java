package com.wu.blog.system.dao;

import com.wu.blog.system.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysLogMapper {
    void insert(SysLog sysLog);

    List<SysLog> selectAll();

    int count();
}
