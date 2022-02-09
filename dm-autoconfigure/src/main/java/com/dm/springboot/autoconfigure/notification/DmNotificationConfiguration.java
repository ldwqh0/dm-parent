package com.dm.springboot.autoconfigure.notification;

import com.dm.notification.controller.NotificationController;
import com.dm.notification.entity.Notification;
import com.dm.notification.repository.NotificationRepository;
import com.dm.notification.service.NotificationService;
import com.dm.notification.service.impl.NotificationServiceImpl;
import com.dm.springboot.autoconfigure.DmEntityScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;

/**
 * 自动配置事件通知功能
 */
@DmEntityScan("com.dm.notification")
@ConditionalOnClass(Notification.class)
public class DmNotificationConfiguration {

    @Bean
    public NotificationRepository notificationRepository(EntityManager em) {
        return new JpaRepositoryFactory(em).getRepository(NotificationRepository.class);
    }

    @Bean
    public NotificationService notificationService(NotificationRepository notificationRepository) {
        return new NotificationServiceImpl(notificationRepository);
    }

    @Bean
    public NotificationController notificationController(NotificationService notificationService) {
        return new NotificationController(notificationService);
    }

}
