package com.wu.blog.system.service;

import com.github.pagehelper.PageHelper;
import com.wu.blog.common.shiro.realm.UserNameRealm;
import com.wu.blog.system.dao.RoleMapper;
import com.wu.blog.system.dao.RoleMenuMapper;
import com.wu.blog.system.dao.RoleOperatorMapper;
import com.wu.blog.system.dao.UserRoleMapper;
import com.wu.blog.system.entity.Role;
import com.wu.blog.system.entity.RoleOperator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleService {

    @Resource
    private RoleMapper roleDao;
    @Resource
    private UserRoleMapper userRoleDao;
    @Resource
    private RoleMenuMapper roleMenuDao;
    @Resource
    private RoleOperatorMapper roleOperatorDao;
    @Resource
    private UserNameRealm userNameRealm;

    public List<Role> selectAll(int page, int limit, Role roleQuery) {
        PageHelper.startPage(page,limit);
        return roleDao.selectAllByQuery(roleQuery);
    }

    public List<Role> selectAll() {
        return roleDao.selectAll();
    }

    @Transactional
    public void add(Role role) {
        roleDao.insert(role);
    }

    public Role selectOne(Integer roleId) {
        return roleDao.selectById(roleId);
    }

    @Transactional
    public void update(Role role) {
        roleDao.update(role);
    }

    @Transactional
    public void delete(Integer roleId) {
        userRoleDao.deleteUserRoleByUserId(roleId);
        roleDao.deleteById(roleId);
        roleMenuDao.deleteByRoleId(roleId);
        roleOperatorDao.deleteByRoleId(roleId);

    }

    /**
     * 为角色分配菜单
     * @param roleId
     * @param menuIds
     */
    @Transactional
    public void grantMenu(Integer roleId, Integer[] menuIds) {
        roleMenuDao.deleteByRoleId(roleId);
        if(menuIds != null && menuIds.length != 0) {
            roleMenuDao.insertRoleMenus(roleId,menuIds);
        }
        clearRoleAuthCache(roleId);
    }

    /**
     * 为角色分配操作权限
     * @param roleId
     * @param operatorIds
     */
    @Transactional
    public void grantOperator(Integer roleId, Integer[] operatorIds) {
        roleOperatorDao.deleteByRoleId(roleId);
        if(operatorIds != null && operatorIds.length != 0) {
            for (int i = 0; i < operatorIds.length; i++) {
                operatorIds[i] = operatorIds[i] - 10000;
            }
            roleOperatorDao.insertRoleOperators(roleId,operatorIds);
        }
        clearRoleAuthCache(roleId);

    }

    private void clearRoleAuthCache(Integer roleId) {
        // 获取该角色下的所有用户
        List<Integer> userIds = userRoleDao.selectUserIdByRoleId(roleId);

        // 将该角色下所有用户的认证缓存清空，以到达刷新认证信息的目的
        for(Integer userId:userIds) {
            userNameRealm.clearAuthCacheByUserId(userId);
        }
    }

    public Integer[] getMenusByRoleId(Integer roleId) {
        return roleMenuDao.getMenusByRoleId(roleId);
    }

    public Integer[] getOperatorsByRoleId(Integer roleId) {
        return roleOperatorDao.getOperatorsByRoleId(roleId);
    }

    public int count() {
        return roleDao.count();
    }
}
