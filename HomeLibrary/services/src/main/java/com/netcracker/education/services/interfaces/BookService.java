package com.netcracker.education.services.interfaces;

import com.netcracker.education.services.dto.BookDTO;
import com.netcracker.education.services.exceptions.LogicException;

import java.util.List;

public interface BookService {

    BookDTO create(BookDTO bookDTO);

    BookDTO search(Integer bookId);

    BookDTO update(BookDTO bookDTO);

    List<BookDTO> getAll();

    List<BookDTO> getTopBooks();

    List<BookDTO> findByAuthorId(Integer authorId);

    List<BookDTO> findByGenre(Integer genreId);

    List<BookDTO> showAllBooksFromUserCard(Integer userCardId);

    List<BookDTO> findByBookSeries(Integer bookSeriesId);

    BookDTO addAuthorToBookById(Integer authorId, Integer bookId);

    List<BookDTO> showAllBooksFromUser(Integer userId);

    BookDTO deleteAuthorFromBookById(Integer authorId, Integer bookId);

    void delete(Integer bookId);
}
