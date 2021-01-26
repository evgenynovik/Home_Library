package com.netcracker.education.services.impl;

import com.netcracker.education.dao.domain.Review;
import com.netcracker.education.dao.domain.UserCard;
import com.netcracker.education.dao.interfaces.BookDAO;
import com.netcracker.education.dao.interfaces.ReviewDAO;
import com.netcracker.education.dao.interfaces.UserCardDAO;
import com.netcracker.education.services.converters.Converter;
import com.netcracker.education.services.dto.MarkDTO;
import com.netcracker.education.services.dto.ReviewDTO;
import com.netcracker.education.services.exceptions.LogicException;
import com.netcracker.education.services.interfaces.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

    private final Converter<Review, ReviewDTO> converter;
    private final ReviewDAO reviewDAO;
    private final BookDAO bookDAO;
    private final UserCardDAO userCardDAO;
    @Value("${rating_bounds.low}")
    private Integer lowLimit;
    @Value("${rating_bounds.high}")
    private Integer highLimit;

    @Autowired
    public ReviewServiceImpl(Converter<Review, ReviewDTO> converter, ReviewDAO reviewDAO, BookDAO bookDAO,
                             UserCardDAO userCardDAO) {
        this.converter = converter;
        this.reviewDAO = reviewDAO;
        this.bookDAO = bookDAO;
        this.userCardDAO = userCardDAO;
    }

    @Override
    @Transactional
    public ReviewDTO create(ReviewDTO reviewDTO) throws LogicException {
        String message = "Your mark is incorrect";
        if (reviewDTO.getRating() > highLimit || reviewDTO.getRating() <= lowLimit) {
            log.error(message);
            throw new LogicException(message);
        }
        if (bookDAO.findById(reviewDTO.getBookId()).isEmpty()) {
            message = "This book is not exist";
            log.error(message);
            throw new LogicException(message);
        }
        if (userCardDAO.findById(reviewDTO.getUserCardId()).isEmpty()) {
            message = "This user card is not exist";
            log.error(message);
            throw new LogicException(message);
        }
        Review reviewToSave = completeConversionToEntity(converter.convertToEntity(reviewDTO), reviewDTO);
        Review reviewFromDAO = reviewDAO.saveAndFlush(reviewToSave);
        log.info(" Review with id = {} is created.", reviewFromDAO.getId());
        return completeConversionToDTO(converter.convertToDTO(reviewFromDAO), reviewFromDAO);
    }

    @Override
    @Transactional
    public ReviewDTO search(Integer reviewId) {
        if (reviewDAO.findById(reviewId).isEmpty()) {
            throw new LogicException("This review is not exist!");
        } else {
            Review review = reviewDAO.findById(reviewId).get();
            log.debug(" Review with id = {} is shown.", reviewId);
            return completeConversionToDTO(converter.convertToDTO(review), review);
        }
    }

    @Override
    @Transactional
    public ReviewDTO update(ReviewDTO reviewDTO) {
        Review review = completeConversionToEntity(converter.convertToEntity(reviewDTO), reviewDTO);
        Review reviewFromDAO = reviewDAO.saveAndFlush(review);
        log.info(" Review with id = {} is updated.", reviewFromDAO.getId());
        return completeConversionToDTO(converter.convertToDTO(reviewFromDAO), reviewFromDAO);
    }

    @Override
    @Transactional
    public List<ReviewDTO> getAll() {
        log.debug(" All reviews are shown.");
        return reviewDAO.findAll()
                .stream()
                .map(review -> completeConversionToDTO(converter.convertToDTO(review), review))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer reviewId) {
        if (reviewDAO.findById(reviewId).isPresent()) {
            reviewDAO.deleteById(reviewId);
            log.info(" Review with id = {} was deleted.", reviewId);
        }
    }

    @Override
    @Transactional
    public List<ReviewDTO> getAllByBookId(Integer bookId) {
        log.debug(" All reviews from book with id = {} are shown.", bookId);
        return reviewDAO.getAllByBookId(bookId).stream()
                .map(review -> completeConversionToDTO(converter.convertToDTO(review), review))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ReviewDTO> showAllReviewsFromUserCard(Integer userCardId) {
        UserCard userCard = userCardDAO.findById(userCardId).orElse(null);
        if (isNull(userCard) || isNull(userCard.getUser())) {
            return Collections.emptyList();
        }
        log.debug(" All reviews from user card with id = {} are shown.", userCardId);
        return userCard.getReviews()
                .stream()
                .filter(Objects::nonNull)
                .map(review -> completeConversionToDTO(converter.convertToDTO(review), review))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteReviewFromUserCard(Integer reviewId) {
        Review review = reviewDAO.findById(reviewId).orElse(null);
        if (nonNull(review)) {
            review.setUserCard(null);
            reviewDAO.saveAndFlush(review);
        }
        log.info(" Review with id = {} was deleted from user card.", reviewId);
    }

    @Override
    public MarkDTO getBookRating(Integer bookId) {
        return new MarkDTO(reviewDAO.getBookRating(bookId));
    }

    @Transactional
    public ReviewDTO completeConversionToDTO(ReviewDTO reviewDTO, Review review) {
        if (nonNull(review)) {
            if (nonNull(review.getBook())) {
                reviewDTO.setBookId(review.getBook().getId());
                reviewDTO.setBookTitle(review.getBook().getTitle());
            }
            if (nonNull(review.getUserCard())) {
                reviewDTO.setUserCardId(review.getUserCard().getId());
            }
        }
        return reviewDTO;
    }

    @Transactional
    public Review completeConversionToEntity(Review review, ReviewDTO reviewDTO) {
        if (nonNull(reviewDTO.getBookId())) {
            review.setBook(bookDAO.findById(reviewDTO.getBookId()).orElse(null));
        }
        if (nonNull(reviewDTO.getUserCardId())) {
            review.setUserCard(userCardDAO.findById(reviewDTO.getUserCardId()).orElse(null));
        }
        return review;
    }
}