package com.netcracker.education.dao.interfaces;

import com.netcracker.education.dao.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewDAO extends JpaRepository<Review, Integer> {

    @Query("SELECT r FROM Review r INNER JOIN r.book b WHERE b.id = ?1")
    List<Review> getAllByBookId(Integer bookId);

    @Query("SELECT avg(r.rating) FROM Review r JOIN r.book b WHERE b.id = ?1")
    Double getBookRating(Integer bookId);
}
