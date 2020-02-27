package com.wu.blog.system.service;


import com.wu.blog.common.shiro.BlogProperties;
import com.wu.blog.common.utils.ShiroUtils;
import com.wu.blog.common.utils.TreeUtil;
import com.wu.blog.system.dao.MenuMapper;
import com.wu.blog.system.dao.OperatorMapper;
import com.wu.blog.system.dao.RoleMenuMapper;
import com.wu.blog.system.entity.Menu;
import com.wu.blog.system.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    @Resource
    private MenuMapper menuDao;
    @Resource
    private OperatorMapper operatorDao;
    @Resource
    private RoleMenuMapper roleMenuDao;
    @Resource
    private BlogProperties blogProperties;

    /**
     * 获取导航菜单中的所有叶子结点
     */
    public List<Menu> getLeafNodeMenu() {
        List<Menu> allMenuTreeVO = getAllTree();
        return TreeUtil.getAllLeafNode(allMenuTreeVO);
    }

    /**
     * 获取所有菜单（树形结构）
     */
    public List<Menu> getAllTree() {
        return menuDao.selectAllTree();
    }

    /**
     * 根据父ID获取所有菜单
     */
    public List<Menu> selectByParentId(Integer parentId) {
        return menuDao.selectByParentId(parentId);
    }

    /**
     * 获取所有菜单并添加根节点（树形结构）
     */
    public List<Menu> getAllMenuTreeAndRoot() {
        List<Menu> allMenuTree = getAllTree();
        return addRootNode("导航目录", 0, allMenuTree);
    }

    /**
     * 将树形结构添加到一个根节点下.
     */
    private List<Menu> addRootNode(String rootName, Integer rootId, List<Menu> children) {
        Menu root = new Menu();
        root.setMenuId(rootId);
        root.setMenuName(rootName);
        root.setChildren(children);
        List<Menu> rootList=new ArrayList<>();
        rootList.add(root);
        return rootList;
    }

    public List<Menu> getAllMenuAndCountOperatorTreeAndRoot() {
        List<Menu> menus=getAllMenuAndCountOperatorTree();
        return addRootNode("导航目录",0,menus);
    }

    /**
     * 获取所有菜单并统计菜单下的操作权限数 (树形结构)
     */
    private List<Menu> getAllMenuAndCountOperatorTree() {
        return menuDao.selectAllMenuAndCountOperator();
    }


    public void insert(Menu menu) {
        int maxOrderNum=menuDao.selectMaxOrderNum();
        menu.setOrderNum(maxOrderNum+1);
        menuDao.insert(menu);
    }

    /**
     * 删除当前菜单及其子菜单
     */
    @Transactional
    public void deleteByIdAndChildren(Integer menuId) {
        // 删除子菜单
        List<Integer> childIdList=menuDao.selectChildrenIdById(menuId);
        for (Integer childId :childIdList) {
            deleteByIdAndChildren(childId);
        }
        // 删除菜单下的操作权限
        operatorDao.deleteByMenuId(menuId);
        // 删除分配给用户的菜单
        roleMenuDao.deleteByMenuId(menuId);
        // 删除自身
        menuDao.delete(menuId);
    }

    public Menu selectById(Integer menuId) {
        return menuDao.selectById(menuId);
    }

    public void update(Menu menu) {
        menuDao.update(menu);
    }

    /**
     * 获取当前登陆用户拥有的树形菜单 (admin 账户拥有所有权限.)
     */
    public List<Menu> selectCurrentUserMenuTree() {
        User user = ShiroUtils.getCurrentUser();
        return selectMenuTreeVOByUsername(user.getUsername());
    }

    /**
     * 获取指定用户拥有的树形菜单 (admin 账户拥有所有权限.)
     */
    List<Menu> selectMenuTreeVOByUsername(String username) {
        List<Menu> menus;
        if(blogProperties.getSuperAdminUsername().equals(username)) {
            menus=menuDao.selectAll();
        }else {
            menus=menuDao.selectMenuByUsername(username);
        }
        return toTree(menus);
    }

    /**
     * 转换为树形结构
     */
    private List<Menu> toTree(List<Menu> menus) {
        return TreeUtil.toTree(menus,"menuId","parentId","children",Menu.class);
    }

    public void swapSort(Integer currentId, Integer swapId) {
        menuDao.swapSort(currentId,swapId);
    }

    public int count() {
        return menuDao.count();
    }
}
