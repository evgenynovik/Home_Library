package com.netcracker.education.controllers.controller;

import com.netcracker.education.services.dto.GenreDTO;
import com.netcracker.education.services.interfaces.GenreService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/library/v1")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @ApiOperation(value = "Create", notes = "(admin operation)")
    @PostMapping("/genres")
    public ResponseEntity<GenreDTO> create(@Valid @RequestBody GenreDTO genreDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.create(genreDTO));
    }

    @ApiOperation(value = "Get a genre by id")
    @GetMapping("/genres/{genreId}")
    public ResponseEntity<GenreDTO> getById(@PathVariable Integer genreId) {
        return ResponseEntity.ok(genreService.search(genreId));
    }

    @ApiOperation(value = "Get all")
    @GetMapping("/genres")
    public ResponseEntity<List<GenreDTO>> getAll() {
        return ResponseEntity.ok(genreService.getAll());
    }

    @ApiOperation(value = "Update", notes = "(admin operation)")
    @PutMapping("/genres")
    public ResponseEntity<GenreDTO> update(@Valid @RequestBody GenreDTO genreDTO) {
        return ResponseEntity.ok(genreService.update(genreDTO));
    }

    @ApiOperation(value = "Delete", notes = "(admin operation)")
    @DeleteMapping("/genres/{genreId}")
    public ResponseEntity<Void> delete(@PathVariable Integer genreId) {
        genreService.delete(genreId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
