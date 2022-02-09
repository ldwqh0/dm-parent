package com.dm.notification.repository;

import com.dm.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select max(n.id) from Notification n")
    Optional<Long> findMaxId();

    Page<Notification> findByUseridAndIdLessThanEqual(Long userid, Long max, Pageable pageable);

}
