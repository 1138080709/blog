package com.wu.blog.system.controller;

import com.wu.blog.common.annotation.OperationLog;
import com.wu.blog.common.constants.AuthcTypeEnum;
import com.wu.blog.common.shiro.OAuth2Helper;
import com.wu.blog.common.utils.ResultBean;
import com.wu.blog.common.utils.ShiroUtils;
import com.wu.blog.system.entity.UserAuths;
import com.wu.blog.system.entity.vo.OAuth2V0;
import com.wu.blog.system.service.UserAuthsService;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("oauth2")
public class OAuth2Controller {

    @Resource
    private OAuth2Helper oAuth2Helper;
    @Resource
    private UserAuthsService userAuthsService;

    /**
     * 生成Github的授权地址
     */
    @OperationLog("Github OAuth2 登录")
    @GetMapping("/render/github")
    @ResponseBody
    public ResultBean renderGithubAuth(HttpServletResponse response) {
        AuthRequest authRequest=oAuth2Helper.getAuthRequest(AuthcTypeEnum.GITHUB);
        return ResultBean.successData(authRequest.authorize());
    }

    /**
     * 生成Gitee的授权地址
     */
    @OperationLog("Gitee OAuth2 登录")
    @GetMapping("/render/gitee")
    @ResponseBody
    public ResultBean renderGiteeAuth(HttpServletResponse response) {
        AuthRequest authRequest=oAuth2Helper.getAuthRequest(AuthcTypeEnum.GITEE);
        return ResultBean.successData(authRequest.authorize());
    }

    @GetMapping("/index")
    public String index() {
        return "oauth2/oauth2-list";
    }

    @OperationLog("获取账号绑定信息")
    @GetMapping("/list")
    @ResponseBody
    public ResultBean list() {
        List<OAuth2V0> authsList = new ArrayList<>();

        for(AuthcTypeEnum type:AuthcTypeEnum.values()) {
            UserAuths auth = userAuthsService.selectOneByIdentityTypeAndUserId(type, ShiroUtils.getCurrentUser().getUserId());

            OAuth2V0 oAuth2V0 = new OAuth2V0();

            oAuth2V0.setType(type.name());
            oAuth2V0.setDescription(type.getDescription());
            oAuth2V0.setStatus(auth == null ? "unbind" : "bind");
            oAuth2V0.setUsername(auth == null ?  "" : auth.getIdentifier());
            authsList.add(oAuth2V0);
        }
        return ResultBean.success(authsList);
    }

    /**
     * 取消授权
     */
    @OperationLog("取消账号绑定")
    @GetMapping("/revoke/{provider}")
    @ResponseBody
    public Object revokeAuth(@PathVariable("provider") AuthcTypeEnum provider) {
        UserAuths userAuths = userAuthsService.selectOneByIdentityTypeAndUserId(provider,ShiroUtils.getCurrentUser().getUserId());

        if(userAuths == null) {
            return ResultBean.error("已经是未绑定状态！");
        }

        userAuthsService.delete(userAuths.getId());
        return ResultBean.success();
    }

    @GetMapping("/success")
    public String success() {
        return "oauth2/authorize-success";
    }

    @GetMapping("/error")
    public String error() {
        return "ouath2/authorize-error";
    }
}
