package com.netcracker.education.controllers.controller;

import com.netcracker.education.controllers.utility.SecurityHelper;
import com.netcracker.education.services.dto.BookDTO;
import com.netcracker.education.services.interfaces.BookService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/library/v1")
public class BookController extends SecurityHelper {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @ApiOperation(value = "Create", notes = "(admin operation)")
    @PostMapping("/books")
    public ResponseEntity<BookDTO> create(@Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(bookDTO));
    }

    @ApiOperation(value = "Get a book by id")
    @GetMapping("/books/{bookId}")
    public ResponseEntity<BookDTO> getById(@PathVariable Integer bookId) {
        return ResponseEntity.ok(bookService.search(bookId));
    }

    @ApiOperation(value = "Get all books from the library")
    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getAll() {
        return ResponseEntity.ok(bookService.getAll());
    }

    @ApiOperation(value = "Update", notes = "(admin operation)")
    @PutMapping("/books")
    public ResponseEntity<BookDTO> update(@Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.update(bookDTO));
    }

    @ApiOperation(value = "Delete", notes = "(admin operation)")
    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<Void> delete(@PathVariable Integer bookId) {
        bookService.delete(bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "Get top books")
    @GetMapping("/books/tops")
    public ResponseEntity<List<BookDTO>> getTopBooks() {
        return ResponseEntity.ok(bookService.getTopBooks());
    }

    @ApiOperation(value = "Show all books of this author")
    @GetMapping("/books/authors/{authorId}")
    public ResponseEntity<List<BookDTO>> findByAuthorId(@PathVariable Integer authorId) {
        return ResponseEntity.ok(bookService.findByAuthorId(authorId));
    }

    @ApiOperation(value = "Show all books of this genre")
    @GetMapping("/books/genres/{genreId}")
    public ResponseEntity<List<BookDTO>> findByGenre(@PathVariable Integer genreId) {
        return ResponseEntity.ok(bookService.findByGenre(genreId));
    }

    @ApiOperation(value = "Show the books you borrow")
    @GetMapping("/books/cards")
    public ResponseEntity<List<BookDTO>> showAllBooksFromUserCard() {
        Integer userCardId = getMyUserDetails().getUserCardId();
        return ResponseEntity.ok(bookService.showAllBooksFromUserCard(userCardId));
    }

    @ApiOperation(value = "Show all books from the book series")
    @GetMapping("/books/series/{bookSeriesId}")
    public ResponseEntity<List<BookDTO>> findByBookSeries(@PathVariable Integer bookSeriesId) {
        return ResponseEntity.ok(bookService.findByBookSeries(bookSeriesId));
    }

    @ApiOperation(value = "Add author to the book", notes = "(admin operation)")
    @PatchMapping("/books/{bookId}/authors/{authorId}")
    public ResponseEntity<BookDTO> addAuthorToBookById(@PathVariable Integer authorId, @PathVariable Integer bookId) {
        return ResponseEntity.ok(bookService.addAuthorToBookById(authorId, bookId));
    }

    @ApiOperation(value = "Show all books the user takes", notes = "(admin operation)")
    @GetMapping("/books/users/{userId}")
    public ResponseEntity<List<BookDTO>> showAllBooksFromUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(bookService.showAllBooksFromUser(userId));
    }

    @ApiOperation(value = "Delete the author from the book", notes = "(admin operation)")
    @DeleteMapping("/books/{bookId}/author/{authorId}")
    public ResponseEntity<BookDTO> deleteAuthorFromBookById(@PathVariable Integer authorId,
                                                            @PathVariable Integer bookId) {
        return ResponseEntity.ok(bookService.deleteAuthorFromBookById(authorId, bookId));
    }
}
