package com.ssafy.algonote.notification.repository;

import com.ssafy.algonote.notification.domain.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByRecieverIdOrderByCreatedAtDesc(Long memberId);
}
