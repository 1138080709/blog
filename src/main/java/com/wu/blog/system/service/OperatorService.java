package com.wu.blog.system.service;

import com.wu.blog.common.utils.TreeUtil;
import com.wu.blog.system.dao.MenuMapper;
import com.wu.blog.system.dao.OperatorMapper;
import com.wu.blog.system.entity.Menu;
import com.wu.blog.system.entity.Operator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class OperatorService {

    @Resource
    private OperatorMapper operatorDao;
    @Resource
    private MenuMapper menuDao;

    public List<Operator> selectAll() {
        return operatorDao.selectAll();
    }

    @Transactional
    public void add(Operator operator) {
        operatorDao.insert(operator);
    }

    public Operator selectById(Integer operatorId) {
        return operatorDao.selectById(operatorId);
    }

    @Transactional
    public int update(Operator operator) {
        return operatorDao.update(operator);
    }

    @Transactional
    public int delete(Integer operatorId) {
        return operatorDao.delete(operatorId);
    }

    /**
     *
     */
    public List<Menu> getAllMenuAndOperatorTree() {

        // 获取用户拥有的所有操作权限
        List<Operator> operators=operatorDao.selectAll();

        List<Menu> menuList=menuDao.selectAll();

        // 获取功能权限树时，菜单应该没有复选框，不可选
        for(Menu menu:menuList) {
            menu.setCheckArr(null);
        }

        List<Menu> menuTree = TreeUtil.toTree(menuList,
                "menuId","parentId","children",Menu.class);



        List<Menu> menuLeafNode=TreeUtil.getAllLeafNode(menuTree);

        // 将操作权限拼接到页面的树形结构下
        for(Menu menu:menuLeafNode) {
            List<Menu> children = menu.getChildren();
            if(children == null) {
                children = new ArrayList<>();
            }

            for(Operator operator : operators) {
                if(menu.getMenuId().equals(operator.getMenuId())) {
                    // 将操作权限转化为 Menu结构，由于操作权限可能与菜单权限的ID值冲突，故将操作权限的ID+10000.
                    // 使用操作权限的ID时再减去则个数
                    Menu temp=new Menu();
                    temp.setMenuId(operator.getOperatorId()+10000);
                    temp.setParentId(operator.getMenuId());
                    temp.setMenuName(operator.getOperatorName());
                    children.add(temp);
                }
            }
            menu.setChildren(children);
        }

        return menuTree;
    }

    public List<Operator> selectByMenuId(Integer menuId) {
        return operatorDao.selectByMenuId(menuId);
    }
}
