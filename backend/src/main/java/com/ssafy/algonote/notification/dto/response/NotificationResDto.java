package com.ssafy.algonote.notification.dto.response;

import com.ssafy.algonote.member.dto.response.MemberResDto;
import com.ssafy.algonote.notification.domain.Notification;
import com.ssafy.algonote.notification.domain.NotificationType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record NotificationResDto(
    Long id,
    NotificationType type,
    String content,
    Long relatedId,
    String relatedTag,
    MemberResDto provider,
    boolean isRead,
    LocalDateTime createdAt
) {
    public static NotificationResDto from(Notification entity) {
        return NotificationResDto.builder()
            .id(entity.getId())
            .type(entity.getNotificationType())
            .content(entity.getContent())
            .relatedId(entity.getRelatedId())
            .relatedTag(entity.getRelatedTag())
            .provider(com.ssafy.algonote.member.dto.response.MemberResDto.from(entity.getProvider()))
            .isRead(entity.isRead())
            .createdAt(entity.getCreatedAt())
            .build();
    }
}