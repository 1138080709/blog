package com.wu.blog.system.service;

import com.github.pagehelper.PageHelper;
import com.wu.blog.common.utils.ShiroUtils;
import com.wu.blog.system.dao.LoginLogMapper;
import com.wu.blog.system.entity.LoginLog;
import com.wu.blog.system.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class LoginLogService {

    @Resource
    private LoginLogMapper loginLogDao;

    public void addLog(String username, boolean authenticated, String ip) {
        LoginLog loginLog=new LoginLog();
        loginLog.setLoginTime(new Date());
        loginLog.setUsername(username);
        loginLog.setLoginStatus(authenticated ? "1" : "0");
        loginLog.setIp(ip);
        loginLogDao.insert(loginLog);

    }

    public List<LoginLog> selectAll(int page, int limit) {
        PageHelper.startPage(page,limit);
        return loginLogDao.selectAll();
    }

    public int count() {
        return loginLogDao.count();
    }

    /**
     * 最近一周登陆次数
     */
    public List<Integer> recentlyWeekLoginCount() {
        User user = ShiroUtils.getCurrentUser();
        return loginLogDao.recentlyWeekLoginCount(user.getUsername());
    }
}
