package com.netcracker.education.services.converters;

import com.netcracker.education.dao.domain.Review;
import com.netcracker.education.services.dto.ReviewDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ReviewConverter implements Converter<Review, ReviewDTO> {

    @Override
    public Review convertToEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setId(reviewDTO.getId());
        review.setContent(reviewDTO.getContent());
        review.setRating(reviewDTO.getRating());
        return review;
    }

    @Override
    public ReviewDTO convertToDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        if (Objects.nonNull(review)) {
            reviewDTO = ReviewDTO.builder()
                    .id(review.getId())
                    .content(review.getContent())
                    .rating(review.getRating()).build();
        }
        return reviewDTO;
    }
}
