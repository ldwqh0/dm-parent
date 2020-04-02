package com.dm.security.oauth2.support.converter;

import java.util.Optional;

import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.stereotype.Component;

import com.dm.common.converter.Converter;
import com.dm.security.oauth2.support.entity.UserClientApproval;

@Component
public class UserApprovalConverter implements Converter<UserClientApproval, Approval> {

    private Approval toDtoActual(UserClientApproval model) {
        Approval approval = new Approval(model.getUserId(), model.getClientId(),
                model.getScope(),
                model.getExpiresAt(),
                model.getStatus());
        approval.setLastUpdatedAt(model.getLastUpdatedAt());
        return approval;
    }

    @Override
    public UserClientApproval copyProperties(UserClientApproval model, Approval dto) {
        model.setClientId(dto.getClientId());
        model.setUserId(dto.getUserId());
        model.setExpiresAt(dto.getExpiresAt());
        model.setScope(dto.getScope());
        model.setStatus(dto.getStatus());
        return model;
    }

    public UserClientApproval newModel(Approval dto) {
        return copyProperties(new UserClientApproval(), dto);
    }

    @Override
    public Approval toDto(UserClientApproval model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }

}
