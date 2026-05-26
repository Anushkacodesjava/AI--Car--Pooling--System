package com.example.demo.repository;

import com.example.demo.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByUserIdAndIsReadFalse(Long userId);
    List<Notification> findAllByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = ?1 AND n.isRead = false")
    long countUnread(Long userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.isRead = false")
    long countUnreadAll();

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.id = ?1 AND n.isRead = false")
    void markAllReadByUserId(Long userId);
}
