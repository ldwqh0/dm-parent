package com.dm.auth.provider.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FrameworkEndpoint
public class RevokeTokenEndpoint {

	@Autowired
	private ConsumerTokenServices tokenService;

	@DeleteMapping("/oauth/token")
	@ResponseBody
	public String r(@RequestParam("access_token") String token) {
		if (tokenService.revokeToken(token)) {
			return "成功";
		} else {
			return "失败";
		}
	}

}
