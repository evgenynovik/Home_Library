package com.netcracker.education.dao.interfaces;

import com.netcracker.education.dao.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookDAO extends JpaRepository<Book, Integer> {

    @Query("SELECT b FROM Book b INNER JOIN b.authors a WHERE a.id = ?1")
    List<Book> findByAuthorId(Integer authorId);

    @Query("SELECT b FROM Book b INNER JOIN b.genre g WHERE g.id = ?1")
    List<Book> findByGenreId(Integer genreId);

    @Query("SELECT b FROM Book b INNER JOIN b.bookSeries s WHERE s.id = ?1")
    List<Book> findByBookSeries(Integer bookSeriesId);

    @Query(value = "SELECT b.* FROM reviews r JOIN books b ON r.book_id = b.id" +
            " GROUP BY b.id ORDER BY avg(r.rating) DESC LIMIT ?1", nativeQuery = true)
    List<Book> getTopBooks(Integer top);

    @Query("SELECT b FROM Book b INNER JOIN b.userCard uc INNER JOIN uc.user u WHERE u.id = ?1")
    List<Book> showAllBooksFromUser(Integer userId);

    @Query("SELECT b FROM Book b INNER JOIN b.userCard uc WHERE uc.id = ?1")
    List<Book> showAllBooksFromUserCard(Integer userCardId);
}
