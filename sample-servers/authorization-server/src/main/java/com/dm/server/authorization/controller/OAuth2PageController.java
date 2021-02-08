package com.dm.server.authorization.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@SessionAttributes({"authorizationRequest"})
@Controller
public class OAuth2PageController {
    @GetMapping({"/oauth2/login.html", "/oauth2/login.html"})
    public String login() {
        return "oauth/login.html";
    }


    @GetMapping({"/oauth2/logout", "/oauth2/logout.html"})
    public String logout() {
        return "oauth/login.html";
    }

    /**
     * 在AuthorizationEndpoint有几个@ExceptionHandler
     *
     * @return
     */
    @GetMapping("/oauth2/error")
    public ModelAndView error(@RequestAttribute("error") Exception error,
                              @RequestParam(value = "client_id", required = false) String client,
                              @RequestParam(value = "redirect_uri", required = false) String redirect) {
        ModelAndView mv = new ModelAndView("oauth/error.html");
        String message = "";
//        if (error instanceof RedirectMismatchException) {
//            message = "请求的redirect_uri: [" + redirect + "] 与该应用(" + client + ")配置的redirect_uri不匹配";
//        } else {
        message = error.getMessage();
//        }
        mv.addObject("message", message);
        return mv;
    }

    /**
     * 首页直接跳转
     *
     * @return
     */
    @GetMapping({"/", "/oauth2/"})
    public String index() {
        return "redirect:/oauth2/index.html";
    }

    @GetMapping({"/oauth2/users/current"})
    @ResponseBody
    public UserDetails getCurrentUser(@AuthenticationPrincipal UserDetails currentUser) {
        return currentUser;
    }
}
