package com.dm.auth.converter;

import java.util.Date;

import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.stereotype.Component;

import com.dm.auth.entity.UserApproval;
import com.dm.common.converter.AbstractConverter;

@Component
public class UserApprovalConverter extends AbstractConverter<UserApproval, Approval> {

    @Override
    protected Approval toDtoActual(UserApproval model) {
        Approval approval = new Approval(model.getUserId(), model.getClientId(),
                model.getScope(),
                model.getExpiresAt(),
                model.getStatus());
        approval.setLastUpdatedAt(Date.from(model.getLastUpdatedAt().toInstant()));
        return approval;
    }

    @Override
    public UserApproval copyProperties(UserApproval model, Approval dto) {
        model.setClientId(dto.getClientId());
        model.setUserId(dto.getUserId());
        model.setExpiresAt(dto.getExpiresAt());
        model.setScope(dto.getScope());
        model.setStatus(dto.getStatus());
        return model;
    }

}
