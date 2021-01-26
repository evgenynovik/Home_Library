package com.netcracker.education.services.test;

import com.netcracker.education.dao.domain.Book;
import com.netcracker.education.dao.domain.Review;
import com.netcracker.education.dao.domain.User;
import com.netcracker.education.dao.domain.UserCard;
import com.netcracker.education.dao.interfaces.BookDAO;
import com.netcracker.education.dao.interfaces.ReviewDAO;
import com.netcracker.education.dao.interfaces.UserCardDAO;
import com.netcracker.education.services.converters.ReviewConverter;
import com.netcracker.education.services.dto.MarkDTO;
import com.netcracker.education.services.dto.ReviewDTO;
import com.netcracker.education.services.exceptions.LogicException;
import com.netcracker.education.services.impl.ReviewServiceImpl;
import com.netcracker.education.services.interfaces.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ReviewConverter.class,
        ReviewServiceImpl.class})
@ActiveProfiles("test")
public class ReviewServiceImplTest {

    @Autowired
    private ReviewService reviewService;
    @MockBean
    private BookDAO bookDAO;
    @MockBean
    private UserCardDAO userCardDAO;
    @MockBean
    private ReviewDAO reviewDAO;
    private final ReviewDTO reviewDTO = ReviewDTO.builder()
            .id(5)
            .content("well")
            .rating(9)
            .bookId(4)
            .bookTitle("The Hobbit")
            .userCardId(1).build();
    private final ReviewDTO secondReviewDTO = ReviewDTO.builder()
            .content("incredible")
            .rating(10)
            .bookId(12)
            .bookTitle("Dracula")
            .userCardId(3).build();

    @BeforeEach
    public void setUp() {
        Review review = new Review("well", 9, null, null);
        Book book = new Book("The Hobbit", 1994, "amazing",
                null, Collections.emptySet(), Collections.emptySet(), null, null);
        UserCard userCard = new UserCard(null, false,
                Collections.emptySet(), Collections.singleton(review), new User("jacob", "bugv",
                "user", null));
        Review primaryReview = new Review();

        book.setId(4);
        review.setId(5);
        userCard.setId(1);
        review.setBook(book);
        review.setUserCard(userCard);
        book.setUserCard(userCard);
        primaryReview.setBook(book);
        primaryReview.setUserCard(userCard);
        primaryReview.setContent("well");
        primaryReview.setRating(9);

        when(reviewDAO.saveAndFlush(primaryReview)).thenReturn(review);
        when(reviewDAO.saveAndFlush(review)).thenReturn(review);
        when(reviewDAO.findById(5)).thenReturn(Optional.of(review));
        when(reviewDAO.findAll()).thenReturn(Collections.singletonList(review));
        when(bookDAO.findById(4)).thenReturn(Optional.of(book));
        when(userCardDAO.findById(1)).thenReturn(Optional.of(userCard));
        when(reviewDAO.getAllByBookId(4)).thenReturn(Collections.singletonList(review));
        when(reviewDAO.getBookRating(4)).thenReturn(7.0);
    }

    @Test()
    public void createTest() {
        ReviewDTO primaryReviewDTO = ReviewDTO.builder()
                .content("well")
                .rating(9)
                .bookId(4)
                .bookTitle("The Hobbit")
                .userCardId(1).build();

        assertDoesNotThrow(() -> assertEquals(reviewDTO, reviewService.create(primaryReviewDTO)));

        primaryReviewDTO.setRating(11);

        assertEquals(assertThrows(LogicException.class, () ->
                reviewService.create(primaryReviewDTO)).getMessage(), "Your mark is incorrect");

        when(userCardDAO.findById(5)).thenReturn(Optional.empty());
        primaryReviewDTO.setRating(9);
        primaryReviewDTO.setUserCardId(5);

        assertEquals(assertThrows(LogicException.class, () ->
                reviewService.create(primaryReviewDTO)).getMessage(), "This user card is not exist");

        when(bookDAO.findById(5)).thenReturn(Optional.empty());
        primaryReviewDTO.setUserCardId(1);
        primaryReviewDTO.setBookId(3);

        assertEquals(assertThrows(LogicException.class, () ->
                reviewService.create(primaryReviewDTO)).getMessage(), "This book is not exist");
    }

    @Test
    public void searchTest() {
        assertEquals(reviewDTO, reviewService.search(5));
        assertEquals("This review is not exist!", assertThrows(LogicException.class,
                () -> reviewService.search(3)).getMessage());
        verify(reviewDAO, times(2)).findById(5);
        verify(reviewDAO).findById(3);
    }

    @Test
    public void updateTest() {
        assertEquals(reviewDTO, reviewService.update(reviewDTO));
        assertNotEquals(secondReviewDTO, reviewService.update(reviewDTO));
        verify(reviewDAO, times(2)).saveAndFlush(any(Review.class));
    }

    @Test
    public void getAllTest() {
        assertEquals(Collections.singletonList(reviewDTO), reviewService.getAll());
        verify(reviewDAO).findAll();
    }

    @Test
    public void deleteTest() {
        reviewService.delete(5);
        verify(reviewDAO).deleteById(5);
    }

    @Test
    public void getAllByBookIdTest() {
        assertEquals(Collections.singletonList(reviewDTO), reviewService.getAllByBookId(4));
        assertNotEquals(Collections.singletonList(secondReviewDTO), reviewService.getAllByBookId(4));
        verify(reviewDAO, times(2)).getAllByBookId(4);
    }

    @Test
    public void showAllReviewsFromUserCardTest() {
        assertEquals(Collections.singletonList(reviewDTO), reviewService.showAllReviewsFromUserCard(1));
        assertNotEquals(Collections.singletonList(secondReviewDTO),
                reviewService.showAllReviewsFromUserCard(1));
        assertEquals(Collections.emptyList(), reviewService.showAllReviewsFromUserCard(2));
        verify(userCardDAO, times(2)).findById(1);
        verify(userCardDAO).findById(2);
    }

    @Test
    public void deleteReviewFromUserCardTest() {
        reviewService.deleteReviewFromUserCard(5);
        verify(reviewDAO).findById(5);
        verify(reviewDAO).saveAndFlush(any());
    }

    @Test
    public void getBookRatingTest() {
        MarkDTO markDTO = new MarkDTO(7.0);

        assertEquals(markDTO, reviewService.getBookRating(4));
        verify(reviewDAO).getBookRating(4);
    }
}
