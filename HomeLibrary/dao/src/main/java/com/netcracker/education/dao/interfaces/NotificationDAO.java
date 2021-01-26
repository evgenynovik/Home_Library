package com.netcracker.education.dao.interfaces;

import com.netcracker.education.dao.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationDAO extends JpaRepository<Notification, Integer> {
}
