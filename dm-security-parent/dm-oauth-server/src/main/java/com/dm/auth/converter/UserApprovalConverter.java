package com.dm.auth.converter;

import java.util.Optional;

import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.stereotype.Component;

import com.dm.auth.entity.UserApproval;
import com.dm.common.converter.Converter;

@Component
public class UserApprovalConverter implements Converter<UserApproval, Approval> {

    private Approval toDtoActual(UserApproval model) {
        Approval approval = new Approval(model.getUserId(), model.getClientId(),
                model.getScope(),
                model.getExpiresAt(),
                model.getStatus());
        approval.setLastUpdatedAt(model.getLastUpdatedAt());
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

    @Override
    public Approval toDto(UserApproval model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }

}
