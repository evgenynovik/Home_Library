package com.netcracker.education.services.impl;

import com.netcracker.education.dao.domain.Author;
import com.netcracker.education.dao.domain.Book;
import com.netcracker.education.dao.interfaces.*;
import com.netcracker.education.services.converters.Converter;
import com.netcracker.education.services.dto.BookDTO;
import com.netcracker.education.services.exceptions.LogicException;
import com.netcracker.education.services.interfaces.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final Converter<Book, BookDTO> converter;
    private final BookDAO bookDAO;
    private final GenreDAO genreDAO;
    private final AuthorDAO authorDAO;
    private final BookSeriesDAO bookSeriesDAO;
    private final UserCardDAO userCardDAO;
    private final ReviewDAO reviewDAO;
    @Value("${books.top}")
    private Integer top;

    @Autowired
    public BookServiceImpl(Converter<Book, BookDTO> converter, BookDAO bookDAO, GenreDAO genreDAO,
                           AuthorDAO authorDAO, BookSeriesDAO bookSeriesDAO, UserCardDAO userCardDAO,
                           ReviewDAO reviewDAO/*, @Qualifier("valid") Validator validator*/) {
        this.converter = converter;
        this.bookDAO = bookDAO;
        this.genreDAO = genreDAO;
        this.authorDAO = authorDAO;
        this.bookSeriesDAO = bookSeriesDAO;
        this.userCardDAO = userCardDAO;
        this.reviewDAO = reviewDAO;
    }

    @Override
    @Transactional
    public BookDTO create(BookDTO bookDTO) {
        Book convertedBook = converter.convertToEntity(bookDTO);
        Book bookFromDao = bookDAO.saveAndFlush(completeConversionToEntity(convertedBook, bookDTO));
        log.info(" Book with id = {} is created.", bookFromDao.getId());
        return completeConversionToDTO(converter.convertToDTO(bookFromDao), bookFromDao);
    }

    @Override
    @Transactional
    public BookDTO search(Integer bookId) {
        if (bookDAO.findById(bookId).isEmpty()) {
            throw new LogicException("This book is not exist!");
        } else {
            Book book = bookDAO.findById(bookId).get();
            log.debug(" Book with id = {} is shown.", bookId);
            return completeConversionToDTO(converter.convertToDTO(book), book);
        }
    }

    @Override
    @Transactional
    public BookDTO update(BookDTO bookDTO) {
        Book book = converter.convertToEntity(bookDTO);
        Book bookFromDAO = bookDAO.saveAndFlush(completeConversionToEntity(book, bookDTO));
        log.info(" Book with id = {} is updated.", bookFromDAO.getId());
        return completeConversionToDTO(converter.convertToDTO(bookFromDAO), bookFromDAO);
    }

    @Override
    public void delete(Integer bookId) {
        if (bookDAO.findById(bookId).isPresent()) {
            bookDAO.deleteById(bookId);
            log.info(" Book with id = {} was deleted.", bookId);
        }
    }

    @Override
    @Transactional
    public List<BookDTO> getAll() {
        log.debug(" All books are shown.");
        return bookDAO.findAll()
                .stream()
                .map(book -> completeConversionToDTO(converter.convertToDTO(book), book))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDTO> getTopBooks() {
        log.debug(" Top books are shown.");
        return bookDAO.getTopBooks(top).stream()
                .map(book -> completeConversionToDTO(converter.convertToDTO(book), book))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDTO> findByAuthorId(Integer authorId) {
        log.debug(" All books by author with id = {} are shown.", authorId);
        return bookDAO.findByAuthorId(authorId).stream()
                .map(book -> completeConversionToDTO(converter.convertToDTO(book), book))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDTO> findByGenre(Integer genreId) {
        log.debug(" All books by genre with id = {} are shown.", genreId);
        return bookDAO.findByGenreId(genreId).stream()
                .map(book -> completeConversionToDTO(converter.convertToDTO(book), book))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDTO> showAllBooksFromUserCard(Integer userCardId) {
        log.debug(" All books from user card with id = {} are shown.", userCardId);
        return bookDAO.showAllBooksFromUserCard(userCardId)
                .stream()
                .map(book -> completeConversionToDTO(converter.convertToDTO(book), book))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDTO> findByBookSeries(Integer bookSeriesId) {
        log.debug(" All books from book series with id = {} are shown.", bookSeriesId);
        return bookDAO.findByBookSeries(bookSeriesId).stream()
                .map(book -> completeConversionToDTO(converter.convertToDTO(book), book))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDTO addAuthorToBookById(Integer authorId, Integer bookId) {
        Book book = bookDAO.findById(bookId).orElse(null);
        Author author = authorDAO.findById(authorId).orElse(null);
        if (nonNull(book)) {
            Set<Author> authors = book.getAuthors();
            authors.add(author);
            book.setAuthors(authors);
            bookDAO.saveAndFlush(book);
        }
        log.info(" Book with id = {} was populated by an author.", bookId);
        return completeConversionToDTO(converter.convertToDTO(book), book);
    }

    @Override
    @Transactional
    public List<BookDTO> showAllBooksFromUser(Integer userId) {
        List<Book> books = bookDAO.showAllBooksFromUser(userId);
        log.debug(" All books from user with id = {} are shown.", userId);
        return books.stream()
                .map(book -> completeConversionToDTO(converter.convertToDTO(book), book))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDTO deleteAuthorFromBookById(Integer authorId, Integer bookId) {
        Book book = bookDAO.findById(bookId).orElse(null);
        Author author = authorDAO.findById(authorId).orElse(null);
        Book alteredBook = null;
        if (nonNull(book)) {
            Set<Author> authors = book.getAuthors();
            authors.remove(author);
            book.setAuthors(authors);
            alteredBook = bookDAO.saveAndFlush(book);
        }
        log.info(" Book with id = {} was freed from the author.", bookId);
        return completeConversionToDTO(converter.convertToDTO(alteredBook), alteredBook);
    }

    @Transactional
    public BookDTO completeConversionToDTO(BookDTO bookDTO, Book book) {
        if (nonNull(book)) {
            if (nonNull(book.getGenre())) {
                bookDTO.setGenreId(book.getGenre().getId());
                bookDTO.setGenreTitle(book.getGenre().getTitle());
            }
            Set<Integer> authorsId = book.getAuthors()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(Author::getId)
                    .collect(Collectors.toSet());
            bookDTO.setAuthorsId(authorsId);
            Set<String> authorsNames = book.getAuthors()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(author -> author.getFirstName() + " " + author.getLastName())
                    .collect(Collectors.toSet());
            bookDTO.setAuthorsName(authorsNames);
            if (nonNull(book.getBookSeries())) {
                bookDTO.setBookSeriesId(book.getBookSeries().getId());
                bookDTO.setBookSeriesTitle(book.getBookSeries().getTitle());
            }
            if (nonNull(book.getUserCard())) {
                bookDTO.setUserCardId(book.getUserCard().getId());
            }
            bookDTO.setRating(reviewDAO.getBookRating(book.getId()));
        }
        return bookDTO;
    }

    @Transactional
    public Book completeConversionToEntity(Book book, BookDTO bookDTO) {
        if (nonNull(bookDTO.getGenreId())) {
            book.setGenre(genreDAO.findById(bookDTO.getGenreId()).orElse(null));
        }
        Set<Author> authors = bookDTO.getAuthorsId()
                .stream()
                .filter(Objects::nonNull)
                .map(authorId -> authorDAO.findById(authorId).orElse(null))
                .collect(Collectors.toSet());
        book.setAuthors(authors);
        if (nonNull(bookDTO.getBookSeriesId())) {
            book.setBookSeries(bookSeriesDAO.findById(bookDTO.getBookSeriesId()).orElse(null));
        }
        if (nonNull(bookDTO.getUserCardId())) {
            book.setUserCard(userCardDAO.findById(bookDTO.getUserCardId()).orElse(null));
        }
        return book;
    }
}
