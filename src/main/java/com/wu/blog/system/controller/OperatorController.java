package com.wu.blog.system.controller;

import com.wu.blog.common.annotation.OperationLog;
import com.wu.blog.common.annotation.RefreshFilterChain;
import com.wu.blog.common.utils.ResultBean;
import com.wu.blog.system.entity.Operator;
import com.wu.blog.system.service.OperatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.transform.Result;
import java.util.List;

@Controller
@RequestMapping("/operator")
public class OperatorController {

    @Resource
    private OperatorService operatorService;

    @OperationLog("查看操作日志")
    @GetMapping("/index")
    public String index() {return "operator/operator-list";}

    @GetMapping
    public String add() {return "operator/operator-add";}

    @RefreshFilterChain
    @PostMapping
    @ResponseBody
    public ResultBean add(Operator operator) {
        operatorService.add(operator);
        return ResultBean.success();
    }

    @GetMapping("/{operatorId}")
    public String update(Model model,@PathVariable("operatorId")Integer operatorId) {
        Operator operator=operatorService.selectById(operatorId);
        model.addAttribute("operator",operator);
        return "operator/operator-add";
    }

    @RefreshFilterChain
    @PutMapping
    @ResponseBody
    public ResultBean update(Operator operator) {
        operatorService.update(operator);
        return ResultBean.success();
    }
    @GetMapping("/list")
    @ResponseBody
    public ResultBean getList(@RequestParam(required = false) Integer menuId) {
        List<Operator> operatorList = operatorService.selectByMenuId(menuId);
        return ResultBean.success(operatorList);
    }   
    
    @RefreshFilterChain
    @DeleteMapping("/{operatorId}")
    @ResponseBody
    public ResultBean delete(@PathVariable("operatorId") Integer operatorId) {
        operatorService.delete(operatorId);
        return ResultBean.success();
    }

    @GetMapping("/tree")
    @ResponseBody
    public ResultBean tree() {
        return ResultBean.success(operatorService.getAllMenuAndOperatorTree());
    }
}
