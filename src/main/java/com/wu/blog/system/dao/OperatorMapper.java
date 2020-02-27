package com.wu.blog.system.dao;

import com.wu.blog.system.entity.Operator;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OperatorMapper {

    /**
     * 获取所有菜单
     * @return
     */
    List<Operator> selectAll();

    int insert(Operator operator);

    Operator selectById(Integer operatorId);

    int update(Operator operator);

    int delete(Integer operatorId);

    int deleteByMenuId(Integer menuId);

    List<Operator> selectByMenuId(Integer menuId);
}
