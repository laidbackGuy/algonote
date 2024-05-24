package com.ssafy.algonote.notification.dto.request;

import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.notification.domain.NotificationType;
import lombok.Builder;

@Builder
public record NotificationReqDto(
    NotificationType notificationType,
    Member receiver,
    Member provider,
    Long relatedId,
    String relatedTag,
    String content
) {
}