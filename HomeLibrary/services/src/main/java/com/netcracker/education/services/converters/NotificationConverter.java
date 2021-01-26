package com.netcracker.education.services.converters;

import com.netcracker.education.dao.domain.Notification;
import com.netcracker.education.services.dto.NotificationDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class NotificationConverter implements Converter<Notification, NotificationDTO> {
    @Override
    public Notification convertToEntity(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setId(notificationDTO.getId());
        notification.setUserCardId(notificationDTO.getUserCardId());
//        notification.setDate(notificationDTO.getDate());
        return notification;
    }

    @Override
    public NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        if (Objects.nonNull(notification)) {
            notificationDTO = NotificationDTO.builder()
                    .id(notification.getId())
                    .userCardId(notification.getUserCardId())
                    .date(notification.getDate())
                    .build();
        }
        return notificationDTO;
    }
}
