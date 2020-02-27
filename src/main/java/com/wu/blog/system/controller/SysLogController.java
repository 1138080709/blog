package com.wu.blog.system.controller;

import com.github.pagehelper.PageInfo;
import com.wu.blog.common.annotation.OperationLog;
import com.wu.blog.common.utils.PageResultBean;
import com.wu.blog.system.entity.Menu;
import com.wu.blog.system.entity.SysLog;
import com.wu.blog.system.service.MenuService;
import com.wu.blog.system.service.SysLogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/log/sys")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    @GetMapping("/index")
    public String index() {
        return "log/sys-logs";
    }

    @OperationLog("查看操作日志")
    @GetMapping("/list")
    @ResponseBody
    public PageResultBean<SysLog> getList(@RequestParam(value="page", defaultValue = "1") int page,
                                          @RequestParam(value="limit",defaultValue = "10") int limit) {
        List<SysLog> sysLogs=sysLogService.selectAll(page,limit);
        PageInfo<SysLog> rolePageInfo=new PageInfo<>(sysLogs);
        return new PageResultBean<>(rolePageInfo.getTotal(),rolePageInfo.getList());
    }
}
