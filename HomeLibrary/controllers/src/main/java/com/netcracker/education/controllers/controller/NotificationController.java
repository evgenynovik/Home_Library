package com.netcracker.education.controllers.controller;

import com.netcracker.education.controllers.utility.SecurityHelper;
import com.netcracker.education.services.dto.NotificationDTO;
import com.netcracker.education.services.interfaces.NotificationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/library/v1")
public class NotificationController extends SecurityHelper {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ApiOperation(value = "Notify admin", notes = "user operation")
    @PostMapping("/notifications")
    public ResponseEntity<NotificationDTO> create() {
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .userCardId(getMyUserDetails().getUserCardId()).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.create(notificationDTO));
    }

    @ApiOperation(value = "Get a notification by id")
    @GetMapping("/notifications/{notificationId}")
    public ResponseEntity<NotificationDTO> search(@PathVariable Integer notificationId) {
        return ResponseEntity.ok(notificationService.search(notificationId));
    }

    @ApiOperation(value = "Get all")
    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationDTO>> getAll() {
        return ResponseEntity.ok(notificationService.getAll());
    }

    @ApiOperation(value = "Delete", notes = "(admin operation)")
    @DeleteMapping("/notifications/{notificationId}")
    public ResponseEntity<Void> delete(@PathVariable Integer notificationId) {
        notificationService.delete(notificationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
