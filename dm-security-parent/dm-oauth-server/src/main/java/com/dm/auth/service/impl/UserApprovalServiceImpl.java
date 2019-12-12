package com.dm.auth.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.auth.converter.UserApprovalConverter;
import com.dm.auth.entity.UserApproval;
import com.dm.auth.entity.UserApprovalPK;
import com.dm.auth.repository.UserApprovalRepository;
import com.dm.auth.service.UserApprovalService;

@Service
public class UserApprovalServiceImpl implements UserApprovalService {

    @Autowired
    private UserApprovalRepository userApprovalRepository;

    @Autowired
    private UserApprovalConverter userApprovalConverter;

    @Override
    @Transactional
    public boolean addApprovals(Collection<Approval> approvals) {
        approvals.forEach(this::save);
        return true;
    }

    @Override
    @Transactional
    public boolean revokeApprovals(Collection<Approval> approvals) {
        approvals.forEach(this::delete);
        return true;
    }

    /**
     * 删除授权信息
     * 
     * @param approval
     */
    private void delete(Approval approval) {
        UserApprovalPK key = new UserApprovalPK(approval.getUserId(), approval.getClientId(), approval.getScope());
        userApprovalRepository.deleteById(key);
    }

    /**
     * 保存存授权信息
     * 
     * @param approval
     * @return
     */
    private UserApproval save(Approval approval) {
        UserApprovalPK key = new UserApprovalPK(approval.getUserId(), approval.getClientId(), approval.getScope());
        UserApproval approval_;
        if (userApprovalRepository.existsById(key)) {
            approval_ = userApprovalRepository.getOne(key);
        } else {
            approval_ = new UserApproval();
        }
        userApprovalConverter.copyProperties(approval_, approval);
        return userApprovalRepository.save(approval_);
    }

    @Override
    public Collection<Approval> getApprovals(String userId, String clientId) {
        List<UserApproval> approvals = userApprovalRepository.findByClientIdAndUserId(clientId, userId);
        return userApprovalConverter.toDto(approvals);
    }

}
