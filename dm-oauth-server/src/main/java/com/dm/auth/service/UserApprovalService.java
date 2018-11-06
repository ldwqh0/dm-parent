package com.dm.auth.service;

import org.springframework.security.oauth2.provider.approval.ApprovalStore;

public interface UserApprovalService extends ApprovalStore {

	public void deleteByClient(String clientId);

}
