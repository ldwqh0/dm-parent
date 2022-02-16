package com.dm.notification.service;

import com.dm.collections.CollectionUtils;
import com.dm.data.domain.RangePage;
import com.dm.data.domain.RangePageable;
import com.dm.notification.domain.NotificationRequest;
import com.dm.notification.domain.NotificationResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public interface NotificationService {

    /**
     * 向单个用户发送通知消息
     *
     * @param userid  接收消息的用户
     * @param title   消息标题
     * @param content 消息内容
     */
    @Transactional
    default NotificationResponse save(Long userid, String title, String content) {
        return save(new NotificationRequest(userid, title, content));
    }

    /**
     * 向用户批量发送通知消息
     *
     * @param users   要发送通知的用户
     * @param title   通知标题
     * @param content 通知内容
     */
    @Transactional
    default Collection<NotificationResponse> save(Collection<Long> users, final String title, final String content) {
        if (CollectionUtils.isNotEmpty(users)) {
            return users.stream()
                .map(it -> new NotificationRequest(it, title, content))
                .map(this::save)
                .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 批量保存通知信息
     *
     * @param collection 要保存的通知信息
     */
    default Collection<NotificationResponse> save(Collection<NotificationRequest> collection) {
        return CollectionUtils.transform(collection, this::save);
    }

    /**
     * 保存通知信息
     *
     * @param notification 要保存的通知信息
     */
    NotificationResponse save(NotificationRequest notification);

    RangePage<NotificationResponse, Long> findByUserid(Long userid, RangePageable<Long> pageable);

}
