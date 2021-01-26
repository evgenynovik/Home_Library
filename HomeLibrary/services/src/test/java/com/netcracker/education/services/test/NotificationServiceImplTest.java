package com.netcracker.education.services.test;

import com.netcracker.education.dao.domain.Notification;
import com.netcracker.education.dao.interfaces.NotificationDAO;
import com.netcracker.education.services.converters.NotificationConverter;
import com.netcracker.education.services.dto.NotificationDTO;
import com.netcracker.education.services.exceptions.LogicException;
import com.netcracker.education.services.impl.NotificationServiceImpl;
import com.netcracker.education.services.interfaces.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {NotificationConverter.class,
        NotificationServiceImpl.class})
public class NotificationServiceImplTest {

    @MockBean
    private NotificationDAO notificationDAO;
    @Autowired
    private NotificationService notificationService;
    private final NotificationDTO notificationDTO = NotificationDTO.builder()
            .id(3)
            .date(LocalDate.now())
            .userCardId(1).build();

    @BeforeEach
    public void setUp() {
        Notification notification = new Notification(LocalDate.now(), 1);
        Notification primaryNotification = new Notification(LocalDate.now(), 1);

        notification.setId(3);

        when(notificationDAO.saveAndFlush(primaryNotification)).thenReturn(notification);
        when(notificationDAO.saveAndFlush(notification)).thenReturn(notification);
        when(notificationDAO.findById(3)).thenReturn(Optional.of(notification));
        when(notificationDAO.findAll()).thenReturn(Collections.singletonList(notification));
    }

    @Test
    public void createTest() {
        NotificationDTO primaryNotificationDTO = NotificationDTO.builder()
                .date(LocalDate.now())
                .userCardId(1).build();

        assertEquals(notificationDTO, notificationService.create(primaryNotificationDTO));
        verify(notificationDAO).saveAndFlush(any(Notification.class));
    }

    @Test
    public void searchTest() {
        assertEquals(notificationDTO, notificationService.search(3));
        assertEquals("This notification is not exist!", assertThrows(LogicException.class,
                () -> notificationService.search(7)).getMessage());
        verify(notificationDAO, times(2)).findById(3);
        verify(notificationDAO).findById(7);
    }

    @Test
    public void getAllTest() {
        assertEquals(Collections.singletonList(notificationDTO), notificationService.getAll());
        verify(notificationDAO).findAll();
    }

    @Test
    public void deleteTest() {
        notificationService.delete(3);
        verify(notificationDAO).deleteById(3);
    }
}
