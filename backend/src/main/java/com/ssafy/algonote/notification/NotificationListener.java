package com.ssafy.algonote.notification;

import com.ssafy.algonote.notification.dto.request.NotificationReqDto;
import com.ssafy.algonote.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    @TransactionalEventListener
    public void handleNotification(NotificationReqDto dto) {
        notificationService.notify(
            dto.notificationType(), dto.receiver(), dto.provider(), dto.relatedId(), dto.relatedTag(), dto.content());
    }

}