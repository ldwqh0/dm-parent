package com.dm.auth.security;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;

public class SimpleUserApprovalHandler implements UserApprovalHandler {
	
	private final String approvalParameter = OAuth2Utils.USER_OAUTH_APPROVAL;

	@Override
	public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest,
			Authentication userAuthentication) {
		return authorizationRequest;

	}

	@Override
	public Map<String, Object> getUserApprovalRequest(AuthorizationRequest arg0, Authentication arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isApproved(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
		return authorizationRequest.isApproved();
	}

	@Override
	public AuthorizationRequest updateAfterApproval(AuthorizationRequest authorizationRequest,
			Authentication userAuthentication) {
		// 获取参数，判断用户选择的是允许还是拒绝
		Map<String, String> approvalParameters = authorizationRequest.getApprovalParameters();
		String flag = approvalParameters.get(approvalParameter);
		boolean approved = flag != null && flag.toLowerCase().equals("true");
		if (approved) {
			String client = authorizationRequest.getClientId();
			String user = userAuthentication.getName();
//			userClientApprovalService.save(user,client);
		}
		authorizationRequest.setApproved(approved);
		return authorizationRequest;
	}

}
