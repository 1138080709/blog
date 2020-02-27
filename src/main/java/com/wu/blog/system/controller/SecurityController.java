package com.wu.blog.system.controller;

import com.wu.blog.common.utils.ResultBean;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 安全相关Controller
 */
@Controller
@RequestMapping("/security")
public class SecurityController {
    /**
     * 判断当前登录用户是否有某项权限
     */
    @GetMapping("/hasPermission/{perms}")
    @ResponseBody
    public ResultBean hasPermission(@PathVariable("perms") String perms) {
        return ResultBean.success(SecurityUtils.getSubject().isPermitted(perms));
    }
}
