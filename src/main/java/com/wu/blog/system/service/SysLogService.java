package com.wu.blog.system.service;

import com.github.pagehelper.PageHelper;
import com.wu.blog.system.dao.SysLogMapper;
import com.wu.blog.system.entity.SysLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysLogService {

    @Resource
    private SysLogMapper sysLogDao;

    public List<SysLog> selectAll(int page, int limit) {
        PageHelper.startPage(page,limit);
        return sysLogDao.selectAll();
    }

    public int count() {
        return sysLogDao.count();
    }
}
