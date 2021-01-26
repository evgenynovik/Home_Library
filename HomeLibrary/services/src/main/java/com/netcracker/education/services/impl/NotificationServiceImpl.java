package com.netcracker.education.services.impl;

import com.netcracker.education.dao.domain.Notification;
import com.netcracker.education.dao.interfaces.NotificationDAO;
import com.netcracker.education.services.converters.Converter;
import com.netcracker.education.services.dto.NotificationDTO;
import com.netcracker.education.services.exceptions.LogicException;
import com.netcracker.education.services.interfaces.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDAO notificationDAO;
    private final Converter<Notification, NotificationDTO> converter;

    @Autowired
    public NotificationServiceImpl(NotificationDAO notificationDAO,
                                   Converter<Notification, NotificationDTO> converter) {
        this.notificationDAO = notificationDAO;
        this.converter = converter;
    }

    @Override
    @Transactional
    public NotificationDTO create(NotificationDTO notificationDTO) {
        Notification notification = notificationDAO.saveAndFlush(converter.convertToEntity(notificationDTO));
        return converter.convertToDTO(notification);
    }

    @Override
    public NotificationDTO search(Integer notificationId) {
        if (notificationDAO.findById(notificationId).isEmpty()) {
            throw new LogicException("This notification is not exist!");
        } else {
            Notification notification = notificationDAO.findById(notificationId).get();
            log.debug(" Notification with id = {} is shown.", notificationId);
            return converter.convertToDTO(notification);
        }
    }

    @Override
    public List<NotificationDTO> getAll() {
        return notificationDAO.findAll().stream()
                .map(converter::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer notificationId) {
        if (notificationDAO.findById(notificationId).isPresent()) {
            notificationDAO.deleteById(notificationId);
        }
    }
}
