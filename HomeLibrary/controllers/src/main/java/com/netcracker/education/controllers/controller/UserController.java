package com.netcracker.education.controllers.controller;

import com.netcracker.education.controllers.utility.SecurityHelper;
import com.netcracker.education.services.dto.UserDTO;
import com.netcracker.education.services.interfaces.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/library/v1")
public class UserController extends SecurityHelper {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Registration", notes = "(Anonymous operation)", tags = "REGISTRATION")
    @PostMapping("/users")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userDTO));
    }

    @ApiOperation(value = "View personal profile")
    @GetMapping("/users")
    public ResponseEntity<UserDTO> getById() {
        Integer userId = getMyUserDetails().getUserId();
        return ResponseEntity.ok(userService.search(userId));
    }

    @ApiOperation(value = "Get all users", notes = "(admin operation)")
    @GetMapping("/users/all")
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @ApiOperation(value = "Update")
    @PutMapping("/users")
    public ResponseEntity<UserDTO> update(@Valid @RequestBody UserDTO userDTO) {
        userDTO.setId(getMyUserDetails().getUserId());
        if (Objects.nonNull(userDTO.getUserCardId())) {
            userDTO.setUserCardId(getMyUserDetails().getUserCardId());
        }
        return ResponseEntity.ok(userService.update(userDTO));
    }

    @ApiOperation(value = "Show a person that takes a book", notes = "(admin operation)")
    @GetMapping("/users/borrowers/{bookId}")
    public ResponseEntity<UserDTO> showBookBorrower(@PathVariable Integer bookId) {
        return ResponseEntity.ok(userService.showBookBorrower(bookId));
    }

    @ApiOperation(value = "Delete")
    @DeleteMapping("/users")
    public ResponseEntity<Void> delete() {
        userService.delete(getMyUserDetails().getUserId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
