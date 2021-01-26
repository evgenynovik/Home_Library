package com.netcracker.education.controllers.controller;

import com.netcracker.education.services.dto.BookSeriesDTO;
import com.netcracker.education.services.interfaces.BookSeriesService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/library/v1")
public class BookSeriesController {

    private final BookSeriesService bookSeriesService;

    @Autowired
    public BookSeriesController(BookSeriesService bookSeriesService) {
        this.bookSeriesService = bookSeriesService;
    }

    @ApiOperation(value = "Create", notes = "(admin operation)")
    @PostMapping("/series")
    public ResponseEntity<BookSeriesDTO> create(@Valid @RequestBody BookSeriesDTO bookSeriesDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookSeriesService.create(bookSeriesDTO));
    }

    @ApiOperation(value = "Get series by id")
    @GetMapping("/series/{bookSeriesId}")
    public ResponseEntity<BookSeriesDTO> getById(@PathVariable Integer bookSeriesId) {
        return ResponseEntity.ok(bookSeriesService.search(bookSeriesId));
    }

    @ApiOperation(value = "Update", notes = "(admin operation)")
    @PutMapping("/series")
    public ResponseEntity<BookSeriesDTO> update(@Valid @RequestBody BookSeriesDTO bookSeriesDTO) {
        return ResponseEntity.ok(bookSeriesService.update(bookSeriesDTO));
    }

    @ApiOperation(value = "Delete", notes = "(admin operation)")
    @DeleteMapping("/series/{bookSeriesId}")
    public ResponseEntity<Void> delete(@PathVariable Integer bookSeriesId) {
        bookSeriesService.delete(bookSeriesId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "Get all")
    @GetMapping("/series")
    public ResponseEntity<List<BookSeriesDTO>> getAll() {
        return ResponseEntity.ok(bookSeriesService.getAll());
    }
}
