package com.netcracker.education.controllers.controller;

import com.netcracker.education.services.dto.AuthorDTO;
import com.netcracker.education.services.interfaces.AuthorService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/library/v1")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ApiOperation(value = "Create", notes = "(admin operation)")
    @PostMapping("/authors")
    public ResponseEntity<AuthorDTO> create(@Valid @RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(authorDTO));
    }

    @ApiOperation(value = "Get an author by id")
    @GetMapping("/authors/{authorId}")
    public ResponseEntity<AuthorDTO> getById(@PathVariable Integer authorId) {
        return ResponseEntity.ok(authorService.search(authorId));
    }

    @ApiOperation(value = "Update", notes = "(admin operation)")
    @PutMapping("/authors")
    public ResponseEntity<AuthorDTO> update(@Valid @RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.update(authorDTO));
    }

    @ApiOperation(value = "Get all", notes = "(admin operation)")
    @GetMapping("/authors")
    public ResponseEntity<List<AuthorDTO>> getAll() {
        return ResponseEntity.ok(authorService.getAll());
    }

    @ApiOperation(value = "Get all authors of a book")
    @GetMapping("/authors/books/{bookId}")
    public ResponseEntity<List<AuthorDTO>> getAllByBook(@PathVariable Integer bookId) {
        return ResponseEntity.ok(authorService.getAllByBook(bookId));
    }

    @ApiOperation(value = "Delete", notes = "(admin operation)")
    @DeleteMapping("/authors/{authorId}")
    public ResponseEntity<Void> delete(@PathVariable Integer authorId) {
        authorService.delete(authorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
