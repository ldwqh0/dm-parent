package com.dm.notification.service.impl;

import com.dm.data.domain.RangePage;
import com.dm.data.domain.RangePageable;
import com.dm.notification.converter.NotificationConverter;
import com.dm.notification.domain.NotificationRequest;
import com.dm.notification.domain.NotificationResponse;
import com.dm.notification.entity.Notification;
import com.dm.notification.repository.NotificationRepository;
import com.dm.notification.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public NotificationResponse save(NotificationRequest notification) {
        return NotificationConverter.toDto(notificationRepository.save(new Notification(notification.getTitle(), notification.getUserid(), notification.getContent())));
    }

    @Override
    public RangePage<NotificationResponse, Long> findByUserid(Long userid, RangePageable<Long> pageable) {
        Long max = pageable.getMax().or(notificationRepository::findMaxId).orElse(0L);
        Page<Notification> result = notificationRepository.findByUseridAndIdLessThanEqual(userid, max, pageable);
        return RangePage.of(max, result).map(NotificationConverter::toDto);
    }
}
