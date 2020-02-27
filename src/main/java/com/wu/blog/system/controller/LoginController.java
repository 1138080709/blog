package com.wu.blog.system.controller;

import cn.hutool.core.util.IdUtil;
import com.wu.blog.common.annotation.OperationLog;
import com.wu.blog.common.exception.CaptchaIncorrectException;
import com.wu.blog.common.shiro.BlogProperties;
import com.wu.blog.common.utils.CaptchaUtil;
import com.wu.blog.common.utils.ResultBean;
import com.wu.blog.system.entity.User;
import com.wu.blog.system.service.MailService;
import com.wu.blog.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class LoginController {

    @Resource
    private UserService userService;

    @Resource
    private BlogProperties blogProperties;

    @Resource
    private MailService mailService;

    @Resource
    private TemplateEngine templateEngine;

    @GetMapping("/index")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginVerify", blogProperties.getLoginVerify());
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResultBean login(User user, @RequestParam(value = "captcha", required = false) String captcha) {
        System.out.println(user.getUsername()+":"+user.getPassword()+":"+captcha);
        Subject subject = SecurityUtils.getSubject();

        // 如果开启了登录校验
        if (blogProperties.getLoginVerify()) {
            String realCaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute("captcha");
            System.out.println(captcha);
            System.out.println(realCaptcha);
            // session 中的验证码过期了
            if (realCaptcha == null || !realCaptcha.equals(captcha.toLowerCase())) {
                throw new CaptchaIncorrectException();
            }
        }

        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        subject.login(token);
        userService.updateLastLoginTimeByUsername(user.getUsername());
        return ResultBean.success("登录成功");
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public ResultBean register(User user) {
        userService.checkUserNameExistOnCreate(user.getUsername());
        String activeCode= IdUtil.fastSimpleUUID();
        user.setActiveCode(activeCode);
        user.setStatus("0");

        HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url=request.getScheme()+"://"
                + request.getServerName()
                + ":"
                + request.getServerPort()
                + "/active/"
                + activeCode;
        Context context =new Context();
        context.setVariable("url",url);
        String mailContent=templateEngine.process("mail/registerTemplate",context);
        new Thread(()->mailService.setHTMLMail(user.getEmail(),"朝拾夕逸博客 激活邮件",mailContent)).start();

        //注册后默认的角色，根据自己数据库的角色表Id设置
        Integer[] initRoleIds={2};
        return ResultBean.success(userService.add(user,initRoleIds));
    }

    @OperationLog("注销")
    @GetMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:login";
    }

    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response) throws IOException {
        //定义图形验证码的长、宽、验证码的字符数、干扰元素个数
        CaptchaUtil.Captcha captcha=CaptchaUtil.createCaptcha(160,50,4,10,30);
        Session session=SecurityUtils.getSubject().getSession();
        session.setAttribute("captcha",captcha.getCode());
//        System.out.println("本次验证码为:"+captcha.getCode());
        response.setContentType("image/png");
        OutputStream os=response.getOutputStream();
        ImageIO.write(captcha.getImage(),"png",os);
    }

    //转化为api接口
    @OperationLog("激活注册账号")
    @GetMapping("/active/{token}")
    public String active(@PathVariable("token")String token,Model model) {
        User user=userService.selectByActiveCode(token);
        String msg;
        if(user==null) {
            msg="请求异常,激活地址不存在!";
        }else if("1".equals(user.getStatus())) {
            msg="用户已激活，请勿重复激活!";
        }else {
            msg="激活成功!";
            user.setStatus("1");
            userService.activeUserByUserId(user.getUserId());
        }
        model.addAttribute("msg",msg);
        return "active";
    }

}
