package com.dm.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@SessionAttributes({ "authorizationRequest" })
@Controller
public class AuthorizationController {

	@GetMapping("/oauth/confirm_access")
	public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
//		if (request.getAttribute("_csrf") != null) {
//			model.put("_csrf", request.getAttribute("_csrf"));
//		}
//		model.put("originalRequest", model.get(ORIGINAL_REQUEST));

		return new ModelAndView("oauth2/confirm_access", model);
	}
}
