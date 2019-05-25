package com.dm.auth.provider.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RevokeTokenEndpoint {

	@Autowired
	private ConsumerTokenServices tokenService;

	@DeleteMapping("/oauth/logout")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void revokeToken(@RequestParam("access_token") String token) {
		// 移除token
		tokenService.revokeToken(token);
	}

}
