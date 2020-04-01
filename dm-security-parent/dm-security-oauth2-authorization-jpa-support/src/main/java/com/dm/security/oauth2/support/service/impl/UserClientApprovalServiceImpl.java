package com.dm.security.oauth2.support.service.impl;

import java.util.Collection;

import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.stereotype.Service;

import com.dm.security.oauth2.support.service.UserClientApprovalService;

@Service
public class UserClientApprovalServiceImpl implements UserClientApprovalService {

    @Override
    public boolean addApprovals(Collection<Approval> approvals) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean revokeApprovals(Collection<Approval> approvals) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Collection<Approval> getApprovals(String userId, String clientId) {
        // TODO Auto-generated method stub
        return null;
    }

}
