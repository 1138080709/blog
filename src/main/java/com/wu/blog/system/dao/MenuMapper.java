package com.wu.blog.system.dao;

import com.wu.blog.system.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<Menu> selectAllTree();

    /**
     * 获取所有菜单
     */
    List<Menu> selectAll();

    List<Menu> selectByParentId(Integer parentId);

    List<Menu> selectAllMenuAndCountOperator();

    int insert(Menu menu);

    int selectMaxOrderNum();

    /**
     * 查找某菜单的所有子类 ID
     */
    List<Integer> selectChildrenIdById(Integer menuId);

    Menu selectById(Integer menuId);

    int update(Menu menu);

    int delete(Integer menuId);

    /**
     * 获取某个用户的所拥有的导航菜单
     */
    List<Menu> selectMenuByUsername(String username);

    /**
     * 交换两个菜单的顺序
     */
    int swapSort(@Param("currentId") Integer currentId, @Param("swapId")Integer swapId);

    int count();
}
