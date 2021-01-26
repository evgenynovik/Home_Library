package com.netcracker.education.controllers.controller;

import com.netcracker.education.controllers.utility.SecurityHelper;
import com.netcracker.education.services.dto.UserCardDTO;
import com.netcracker.education.services.interfaces.UserCardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/library/v1")
public class UserCardController extends SecurityHelper {

    private final UserCardService userCardService;

    @Autowired
    public UserCardController(UserCardService userCardService) {
        this.userCardService = userCardService;
    }

    @ApiOperation(value = "Create")
    @PostMapping("/cards")
    public ResponseEntity<UserCardDTO> create(@Valid @RequestBody UserCardDTO userCardDTO) {
        userCardDTO.setUserId(getMyUserDetails().getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userCardService.create(userCardDTO));
    }

    @ApiOperation(value = "View personal card")
    @GetMapping("/cards")
    public ResponseEntity<UserCardDTO> getById() {
        Integer userCardId = getMyUserDetails().getUserCardId();
        return ResponseEntity.ok(userCardService.search(userCardId));
    }

    @ApiOperation(value = "View user card (Admin operation)")
    @GetMapping("/cards/{userCardId}")
    public ResponseEntity<UserCardDTO> getByUserCardId(@PathVariable Integer userCardId) {
        return ResponseEntity.ok(userCardService.search(userCardId));
    }

    @ApiOperation(value = "Update")
    @PutMapping("/cards")
    public ResponseEntity<UserCardDTO> update(@Valid @RequestBody UserCardDTO userCardDTO) {
        userCardDTO.setId(getMyUserDetails().getUserCardId());
        userCardDTO.setUserId(getMyUserDetails().getUserId());
        return ResponseEntity.ok(userCardService.update(userCardDTO));
    }

    @ApiOperation(value = "Get all", notes = "(admin operation)")
    @GetMapping("/cards/all")
    public ResponseEntity<List<UserCardDTO>> getAll() {
        return ResponseEntity.ok(userCardService.getAll());
    }

    @ApiOperation(value = "Add a book to your card")
    @PatchMapping("/cards/books/{bookId}")
    public ResponseEntity<UserCardDTO> addBookToUserCard(@PathVariable Integer bookId) {
        Integer userCardId = getMyUserDetails().getUserCardId();
        return ResponseEntity.ok(userCardService.addBookToUserCard(userCardId, bookId));
    }

    @ApiOperation(value = "Return a book to the library")
    @DeleteMapping("/cards/books/{bookId}")
    public ResponseEntity<Void> returnBookToLibrary(@PathVariable Integer bookId) {
        Integer userCardId = getMyUserDetails().getUserCardId();
        userCardService.returnBookToLibrary(userCardId, bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "Approve user choice", notes = "(admin operation)")
    @PatchMapping("/cards/{userCardId}")
    public ResponseEntity<UserCardDTO> permit(@PathVariable Integer userCardId) {
        return ResponseEntity.ok(userCardService.permit(userCardId));
    }

    @ApiOperation(value = "Delete")
    @DeleteMapping("/cards")
    public ResponseEntity<Void> delete() {
        Integer userCardId = getMyUserDetails().getUserCardId();
        userCardService.delete(userCardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
