package com.netcracker.education.controllers.controller;

import com.netcracker.education.controllers.utility.SecurityHelper;
import com.netcracker.education.services.dto.MarkDTO;
import com.netcracker.education.services.dto.ReviewDTO;
import com.netcracker.education.services.interfaces.ReviewService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/library/v1")
public class ReviewController extends SecurityHelper {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @ApiOperation(value = "Create")
    @PostMapping("/reviews")
    public ResponseEntity<ReviewDTO> create(@Valid @RequestBody ReviewDTO reviewDTO) {
        reviewDTO.setUserCardId(getMyUserDetails().getUserCardId());
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(reviewDTO));
    }

    @ApiOperation(value = "Get a review by id")
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> getById(@PathVariable Integer reviewId) {
        return ResponseEntity.ok(reviewService.search(reviewId));
    }

    @ApiOperation(value = "Get all")
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDTO>> getAll() {
        return ResponseEntity.ok(reviewService.getAll());
    }

    @ApiOperation(value = "Update")
    @PutMapping("/reviews")
    public ResponseEntity<ReviewDTO> update(@Valid @RequestBody ReviewDTO reviewDTO) {
        reviewDTO.setUserCardId(getMyUserDetails().getUserCardId());
        return ResponseEntity.ok(reviewService.update(reviewDTO));
    }

    @ApiOperation(value = "Delete")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> delete(@PathVariable Integer reviewId) {
        reviewService.delete(reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // delete it
    @DeleteMapping("/reviews/{reviewId}/cards")
    public ResponseEntity<Void> deleteReviewFromUserCard(@PathVariable Integer reviewId) {
        reviewService.deleteReviewFromUserCard(reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "Get book rating")
    @GetMapping("/reviews/ratings/{bookId}")
    public ResponseEntity<MarkDTO> getBookRating(@PathVariable Integer bookId) {
        return ResponseEntity.ok(reviewService.getBookRating(bookId));
    }

    @ApiOperation(value = "Show all your reviews")
    @GetMapping("/reviews/cards")
    public ResponseEntity<List<ReviewDTO>> showAllReviewsFromUserCard() {
        Integer userCardId = getMyUserDetails().getUserCardId();
        return ResponseEntity.ok(reviewService.showAllReviewsFromUserCard(userCardId));
    }

    @ApiOperation(value = "Show all book reviews")
    @GetMapping("/reviews/books/{bookId}")
    public ResponseEntity<List<ReviewDTO>> getAllByBookId(@PathVariable Integer bookId) {
        return ResponseEntity.ok(reviewService.getAllByBookId(bookId));
    }
}
