package com.dm.notification.controller;

import com.dm.data.domain.RangePage;
import com.dm.data.domain.RangePageable;
import com.dm.notification.domain.NotificationResponse;
import com.dm.notification.domain.NotificationRequest;
import com.dm.notification.service.NotificationService;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 通知消息
 */
@RestController
@RequestMapping("notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * 新增一个通知消息
     *
     * @param notification 通知消息的实体
     */
    @PostMapping
    public NotificationResponse save(@RequestBody NotificationRequest notification) {
        return notificationService.save(notification);
    }

    /**
     * 获取发送给我的通知消息
     *
     * @param userid   要查询的用户的id, 从系统种获取
     * @param pageable 分页参数
     * @ignoreParams userid
     */
    @GetMapping
    public RangePage<NotificationResponse, Long> find(@AuthenticationPrincipal(expression = "id") Long userid,
                                                      @PageableDefault(sort = "createdTime", direction = Sort.Direction.DESC) RangePageable<Long> pageable
    ) {
        return notificationService.findByUserid(userid, pageable);
    }

}
