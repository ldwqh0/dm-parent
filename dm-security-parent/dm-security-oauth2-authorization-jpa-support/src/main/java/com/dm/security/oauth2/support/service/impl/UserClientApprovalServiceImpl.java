package com.dm.security.oauth2.support.service.impl;

import com.dm.security.oauth2.support.converter.UserApprovalConverter;
import com.dm.security.oauth2.support.entity.UserClientApproval;
import com.dm.security.oauth2.support.repository.UserClientApprovalRepository;
import com.dm.security.oauth2.support.service.UserClientApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserClientApprovalServiceImpl implements UserClientApprovalService {

    @Autowired
    private UserClientApprovalRepository ucaRepository;

    @Autowired
    private UserApprovalConverter uacConverter;

    @Override
    @Transactional
    public boolean addApprovals(Collection<Approval> approvals) {
        Collection<UserClientApproval> results = approvals.stream().map(uacConverter::newModel)
            .collect(Collectors.toList());
        ucaRepository.saveAll(results);
        return true;
    }

    @Override
    @Transactional
    public boolean revokeApprovals(Collection<Approval> approvals) {
        approvals.forEach(app -> ucaRepository.deleteById(app.getClientId(), app.getUserId(), app.getScope()));
        return true;
    }

    @Override
    public Collection<Approval> getApprovals(String userId, String clientId) {
        return ucaRepository.findByClientIdAndUserId(clientId, userId)
            .stream().map(uacConverter::toDto)
            .collect(Collectors.toList());
    }

}
