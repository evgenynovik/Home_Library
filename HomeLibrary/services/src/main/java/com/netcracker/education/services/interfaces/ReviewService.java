package com.netcracker.education.services.interfaces;

import com.netcracker.education.services.dto.MarkDTO;
import com.netcracker.education.services.dto.ReviewDTO;
import com.netcracker.education.services.exceptions.LogicException;

import java.util.List;

public interface ReviewService {

    ReviewDTO create(ReviewDTO reviewDTO) throws LogicException;

    ReviewDTO search(Integer genreId);

    ReviewDTO update(ReviewDTO reviewDTO);

    List<ReviewDTO> getAll();

    void delete(Integer genreId);

    List<ReviewDTO> getAllByBookId(Integer bookId);

    void deleteReviewFromUserCard(Integer reviewId);

    List<ReviewDTO> showAllReviewsFromUserCard(Integer userCardId);

    MarkDTO getBookRating(Integer bookId);
}
