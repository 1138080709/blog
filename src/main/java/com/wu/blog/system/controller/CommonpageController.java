package com.wu.blog.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

@Controller
public class CommonpageController {
    
    @Resource
    private WebApplicationContext applicationContext;
    
    @GetMapping("/403")
    public String forbidden() {
        return "error/403";
    }
    
    @GetMapping("/404") 
    public String unauthorizedPage() {
        return "error/404";
    }
    
    @GetMapping("/500")
    public String error() {
        return "error/500";
    }
}
