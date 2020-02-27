package com.wu.blog.system.service;

import com.github.pagehelper.PageHelper;
import com.wu.blog.common.exception.DuplicateNameException;
import com.wu.blog.common.shiro.BlogProperties;
import com.wu.blog.common.utils.TreeUtil;
import com.wu.blog.system.dao.UserMapper;
import com.wu.blog.system.dao.UserRoleMapper;
import com.wu.blog.system.entity.Menu;
import com.wu.blog.system.entity.User;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private static final Logger log= LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userDao;
    @Resource
    private UserRoleMapper userRoleDao;
    @Resource
    private SessionDAO sessionDAO;
    @Resource
    private BlogProperties blogProperties;
    @Resource
    private MenuService menuService;

    public Set<String> selectRoleNameByUsername(String username) {
        return userDao.selectRoleNameByUsername(username);
    }

    /**
     * 获取用户拥有的所有菜单权限和操作权限.
     * @param username      用户名
     * @return              权限标识符号列表
     */
    public Set<String> selectPermsByUsername(String username) {
        Set<String> perms = new HashSet<>();
        List<Menu> menuTreeVOs = menuService.selectMenuTreeVOByUsername(username);
        List<Menu> leafNodeMenuList = TreeUtil.getAllLeafNode(menuTreeVOs);
        for(Menu menu : leafNodeMenuList) {
            perms.add(menu.getPerms());
        }
        perms.addAll(userDao.selectOperatorPermsByUsername(username));
        return perms;
    }

    public User selectOneByUsername(String username) {
        return userDao.selectOneByUsername(username);
    }

    @Transactional
    public Integer add(User user, Integer[] roleIds) {
        checkUserNameExistOnCreate(user.getUsername());
        String salt=generateSalt();
        String encryptPassword=new Md5Hash(user.getPassword(),salt).toString();

        user.setSalt(salt);
        user.setPassword(encryptPassword);
        userDao.insert(user);

        grantRole(user.getUserId(),roleIds);
        return user.getUserId();
    }

    @Transactional
    public void grantRole(Integer userId, Integer[] roleIds) {
        if(roleIds==null||roleIds.length==0){
            throw new IllegalArgumentException("赋予的角色数组不能为空.");
        }
        // 清空原有的角色，赋予新角色.
        userRoleDao.deleteUserRoleByUserId(userId);
        userRoleDao.insertList(userId,roleIds);
    }

    private String generateSalt() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 新增时校验用户名是否重复
     * @param username  用户名
     */
    public void checkUserNameExistOnCreate(String username) {
        if(userDao.countByUserName(username)>0){
            throw new DuplicateNameException();
        }
    }

    /**
     * 更新时校验用户名是否重复(不包含某用户 ID).
     * @param user
     */
    private void checkUserNameExistOnUpdate(User user) {
        if(userDao.countByUserNameNotIncludeUserId(user.getUsername(),user.getUserId())>0) {
            throw new DuplicateNameException();
        }
    }

    public User selectOne(Integer userId) {
        return userDao.selectByPrimaryKey(userId);
    }

    public void updateLastLoginTimeByUsername(String username) {
        userDao.updateLastLoginTimeByUsername(username);
    }

    public User selectByActiveCode(String token) {
        return userDao.selectByActiveCode(token);
    }

    public void activeUserByUserId(Integer userId) {
        userDao.activeUserByUserId(userId);
    }

    public List<User> selectAllWithInfo(int page, int limit, User userQuery) {
        PageHelper.startPage(page,limit);
        return userDao.selectAllWithInfo(userQuery);
    }

    public Integer[] selectRoleIdsById(Integer userId) {
        return userDao.selectRoleIdsByUserId(userId);
    }

    @Transactional
    public int update(User user, Integer[] roleIds) {
        checkUserNameExistOnUpdate(user);
        grantRole(user.getUserId(),roleIds);
        return userDao.update(user);
    }


    public boolean disableUserById(Integer userId) {
        offlineByUserId(userId);//禁用用户后，将当前在线的用户踢出
        return userDao.updateStatusById(userId,0) == 1;
    }

    public boolean enableUserById(Integer userId) {
        return userDao.updateStatusById(userId,1) == 1;
    }

    /**
     * 删除此用户的所有在线用户
     */
    public void offlineByUserId(Integer userId) {
        Collection<Session> activeSessions=sessionDAO.getActiveSessions();
        for (Session session : activeSessions) {
            SimplePrincipalCollection simplePrincipalCollection=(SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if(simplePrincipalCollection == null) {
                User user=(User)simplePrincipalCollection.getPrimaryPrincipal();
                if(user != null && userId.equals(user.getUserId())) {
                    offlineBySessionId(String.valueOf(session.getId()));
                }
            }
        }
    }

    public void offlineBySessionId(String sessionId) {
        Session session = sessionDAO.readSession(sessionId);
        if(session != null) {
            log.debug("成功踢出 sessionId 为 :" + sessionId + "的用户.");
            session.stop();
            sessionDAO.delete(session);
        }
    }

    @Transactional
    public void delete(Integer userId) {
        //检查删除的是否是超级管理员，如果是，则不允许删除
        User user = userDao.selectByPrimaryKey(userId);
        if(blogProperties.getSuperAdminUsername().equals(user.getUsername())) {
            throw new UnauthorizedException("试图删除超级管理员, 被禁止.");
        }
        //userAuthsService.deleteByUserId(userId);
        userDao.delete(userId);
        userRoleDao.deleteUserRoleByUserId(userId);

    }

    public void updatePasswordById(Integer userId, String password) {
        String salt=generateSalt();
        String encryptPassword = new Md5Hash(password,salt).toString();
        userDao.updatePasswordById(userId,encryptPassword,salt);
    }

    public int count() {
        return userDao.count();
    }
}
