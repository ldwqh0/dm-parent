package com.dm.notification.converter;

import com.dm.notification.domain.NotificationResponse;
import com.dm.notification.entity.Notification;

public final class NotificationConverter {
    private NotificationConverter() {
    }

    public static NotificationResponse toDto(Notification model) {
        return new NotificationResponse(
            model.getId(),
            model.getUserid(),
            model.getTitle(),
            model.getContent(),
            model.getCreatedTime()
        );
    }
}
