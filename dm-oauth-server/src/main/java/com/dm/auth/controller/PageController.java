package com.dm.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@SessionAttributes({ "authorizationRequest" })
@Controller
public class PageController {

	@GetMapping("/oauth/login.html")
	public String login() {
		return "login/login.html";
	}

	@GetMapping("/oauth/confirm_access")
	public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
		return new ModelAndView("oauth2/confirm_access", model);
	}

	@GetMapping({ "/oauth/", "/oauth/index.html" })
	public String index() {
		return "forward:/index.html";
	}

	@RequestMapping("/TermOfValidity")
	@ResponseBody
	public String termOfValidity() {
		return "系统已过试用期,请联系开发商处理";
	}
}
