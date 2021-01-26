package com.netcracker.education.services.interfaces;

import com.netcracker.education.services.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {

    NotificationDTO create(NotificationDTO notificationDTO);

    NotificationDTO search(Integer notificationId);

    List<NotificationDTO> getAll();

    void delete(Integer notificationId);
}
