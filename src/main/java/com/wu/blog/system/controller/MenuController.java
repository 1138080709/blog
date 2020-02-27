package com.wu.blog.system.controller;

import com.wu.blog.common.annotation.OperationLog;
import com.wu.blog.common.annotation.RefreshFilterChain;
import com.wu.blog.common.utils.ResultBean;
import com.wu.blog.system.entity.Menu;
import com.wu.blog.system.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping("/index")
    public String index() {
        return "menu/menu-list";
    }

    @OperationLog("获取菜单列表")
    @GetMapping("/list")
    @ResponseBody
    public ResultBean list(@RequestParam(required = false) Integer parentId) {
        List<Menu> menuList = menuService.selectByParentId(parentId);
        return ResultBean.success(menuList);
    }

    @GetMapping
    public String add(Model model) {
        return "menu/menu-add";
    }

    @GetMapping("/tree")
    @ResponseBody
    public ResultBean tree() {
        return ResultBean.success(menuService.getAllTree());
    }
    
    @GetMapping("/tree/root")
    @ResponseBody
    public ResultBean treeAndRoot() {
        return ResultBean.success(menuService.getAllMenuTreeAndRoot());
    }

    @GetMapping("/tree/root/operator")
    @ResponseBody
    public ResultBean menuAndCountOperatorTreeAndRoot() {
        return ResultBean.success(menuService.getAllMenuAndCountOperatorTreeAndRoot());
    }

    @OperationLog("新增菜单")
    @RefreshFilterChain
    @PostMapping
    @ResponseBody
    public ResultBean add(Menu menu) {
        menuService.insert(menu);
        return ResultBean.success();
    }

    @OperationLog("删除菜单")
    @RefreshFilterChain
    @DeleteMapping("/{menuId}")
    @ResponseBody
    public ResultBean delete(@PathVariable("menuId") Integer menuId) {
        menuService.deleteByIdAndChildren(menuId);
        return ResultBean.success();
    }

    @GetMapping("/{menuId}")
    public String updateMenu(@PathVariable("menuId") Integer menuId,Model model) {
        Menu menu=menuService.selectById(menuId);
        model.addAttribute("menu",menu);
        return "menu/menu-add";
    }

    @OperationLog("修改菜单")
    @RefreshFilterChain
    @PutMapping
    @ResponseBody
    public ResultBean update(Menu menu) {
        menuService.update(menu);
        return ResultBean.success();
    }

    @OperationLog("调整部门排序")
    @PostMapping("/swap")
    @ResponseBody
    public ResultBean swapSort(Integer currentId,Integer swapId) {
        menuService.swapSort(currentId,swapId);
        return ResultBean.success();
    }
}
