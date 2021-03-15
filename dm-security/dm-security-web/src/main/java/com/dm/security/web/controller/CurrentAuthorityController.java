package com.dm.security.web.controller;

import com.dm.security.annotation.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class CurrentAuthorityController {
    @GetMapping("/authorities/current")
    @ResponseBody
    public Object currentUser(@CurrentUser Object p) {
        return p;
    }

    @GetMapping("session")
    @ResponseBody
    public Object session(WebRequest request) {
        return request.getSessionId();
    }
}
