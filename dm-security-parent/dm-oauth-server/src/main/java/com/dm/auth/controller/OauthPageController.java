package com.dm.auth.controller;

import java.util.Map;

import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@SessionAttributes({ "authorizationRequest" })
@Controller
public class OauthPageController {

    @GetMapping("/oauth/login.html")
    public String login() {
        return "oauth/login.html";
    }

    @GetMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model) throws Exception {
        return new ModelAndView("oauth/confirm_access", model);
    }

    /**
     * 在AuthorizationEndpoint有几个@ExceptionHandler
     * 
     * @param model 错误信息
     * @return
     */
    @GetMapping("/oauth/error")
    public ModelAndView error(@RequestAttribute("error") Exception error,
            @RequestParam(value = "client_id", required = false) String client,
            @RequestParam(value = "redirect_uri", required = false) String redirect) {
        ModelAndView mv = new ModelAndView("oauth/error.html");
        String message = "";
        if (error instanceof RedirectMismatchException) {
            message = "请求的redirect_uri: [" + redirect + "] 与该应用(" + client + ")配置的redirect_uri不匹配";
        } else {
            message = error.getMessage();
        }
        mv.addObject("message", message);
        return mv;
    }

    /**
     * 首页直接跳转
     * 
     * @return
     */
    @GetMapping({ "/", "/oauth/" })
    public String index() {
        return "redirect:/oauth/index.html";
    }

}
