package com.dm.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@SessionAttributes({ "authorizationRequest" })
@Controller
public class PageController {

	@GetMapping("/oauth/login.html")
	public String login() {
		return "oauth/login.html";
	}

	@GetMapping("/oauth/confirm_access")
	public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
		return new ModelAndView("oauth/confirm_access", model);
	}

	@GetMapping({ "/oauth/", "/oauth/index.html" })
	public String index() {
		return "forward:/index.html";
	}
}
