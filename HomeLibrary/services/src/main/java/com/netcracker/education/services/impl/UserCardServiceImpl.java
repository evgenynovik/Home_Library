package com.netcracker.education.services.impl;

import com.netcracker.education.dao.domain.Book;
import com.netcracker.education.dao.domain.Review;
import com.netcracker.education.dao.domain.UserCard;
import com.netcracker.education.dao.interfaces.BookDAO;
import com.netcracker.education.dao.interfaces.ReviewDAO;
import com.netcracker.education.dao.interfaces.UserCardDAO;
import com.netcracker.education.dao.interfaces.UserDAO;
import com.netcracker.education.services.converters.Converter;
import com.netcracker.education.services.dto.UserCardDTO;
import com.netcracker.education.services.exceptions.LogicException;
import com.netcracker.education.services.interfaces.UserCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class UserCardServiceImpl implements UserCardService {

    private final Converter<UserCard, UserCardDTO> converter;
    private final UserCardDAO userCardDAO;
    private final BookDAO bookDAO;
    private final ReviewDAO reviewDAO;
    private final UserDAO userDAO;
    @Value("${books.limit}")
    private Integer booksLimit;

    @Autowired
    public UserCardServiceImpl(Converter<UserCard, UserCardDTO> converter, UserCardDAO userCardDAO, BookDAO bookDAO,
                               ReviewDAO reviewDAO, UserDAO userDAO) {
        this.converter = converter;
        this.userCardDAO = userCardDAO;
        this.bookDAO = bookDAO;
        this.reviewDAO = reviewDAO;
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public UserCardDTO create(UserCardDTO userCardDTO) throws LogicException {
        String message = "You already have a personal card!";
        if (Objects.nonNull(userDAO.findById(userCardDTO.getUserId()).orElse(null).getUserCard())) {
            log.error(message);
            throw new LogicException(message);
        }
        UserCard userCard = completeConversionToEntity(converter.convertToEntity(userCardDTO), userCardDTO);
        UserCard userCardFromDAO = userCardDAO.saveAndFlush(userCard);
        log.info("User card with id = {} is created.", userCardFromDAO.getId());
        return completeConversionToDTO(converter.convertToDTO(userCardFromDAO), userCardFromDAO);
    }

    @Override
    @Transactional
    public UserCardDTO search(Integer userCardId) {
        if (userCardDAO.findById(userCardId).isEmpty()) {
            throw new LogicException("This card is not exist!");
        } else {
            UserCard userCard = userCardDAO.findById(userCardId).get();
            log.debug(" User card with id = {} is shown.", userCardId);
            return completeConversionToDTO(converter.convertToDTO(userCard), userCard);
        }
    }

    @Override
    @Transactional
    public UserCardDTO update(UserCardDTO userCardDTO) {
        UserCard convertedUserCard = converter.convertToEntity(userCardDTO);
        UserCard userCard = userCardDAO.saveAndFlush(completeConversionToEntity(convertedUserCard, userCardDTO));
        log.info(" User card with id = {} is updated.", userCard.getId());
        return completeConversionToDTO(converter.convertToDTO(userCard), userCard);
    }

    @Override
    @Transactional
    public List<UserCardDTO> getAll() {
        log.debug(" All user card is shown.");
        return userCardDAO.findAll()
                .stream()
                .map(userCard -> completeConversionToDTO(converter.convertToDTO(userCard), userCard))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer userCardId) {
        if (userCardDAO.findById(userCardId).isPresent()) {
            userCardDAO.deleteById(userCardId);
            log.info(" User card with id = {} was deleted.", userCardId);
        }
    }

    @Override
    @Transactional
    public UserCardDTO addBookToUserCard(Integer userCardId, Integer bookId) throws LogicException {
        Book book = bookDAO.findById(bookId).orElse(null);
        UserCard userCard = userCardDAO.findById(userCardId).orElse(null);
        if (isNull(book) || isNull(userCard)) {
            String message = " This book or this user card is not exist.";
            log.error(message);
            throw new LogicException(message);
        }
        if (nonNull(book.getUserCard())) {
            String message = " This book is occupied by another person.";
            log.error(message);
            throw new LogicException(message);
        }
        if (userCard.getBooks().size() >= booksLimit) {
            String message = String.format(" You can't hold more than %s books at a time", booksLimit);
            log.error(message);
            throw new LogicException(message);
        }
        Set<Book> books = userCard.getBooks();
        books.add(book);
        userCard.setBooks(books);
        userCard.setPermission(false);
        book.setUserCard(userCard);
        bookDAO.saveAndFlush(book);
        UserCardDTO userCardDTO = converter.convertToDTO(userCardDAO.saveAndFlush(userCard));
        log.info(" Book with id = {} was added to user card", bookId);
        return completeConversionToDTO(userCardDTO, userCard);
    }

    @Override
    @Transactional
    public UserCardDTO permit(Integer userCardId) {
        UserCard userCard = userCardDAO.permit(userCardId);
        if (isNull(userCard)) {
            return null;
        }
        return completeConversionToDTO(converter.convertToDTO(userCard), userCard);
    }

    @Override
    @Transactional
    public void returnBookToLibrary(Integer userCardId, Integer bookId) throws LogicException {
        UserCard userCard = userCardDAO.findById(userCardId).orElse(new UserCard());
        Book book = bookDAO.findById(bookId).orElse(new Book());
        Set<Book> books = userCard.getBooks();
        if (!books.contains(book)) {
            String message = " You don't hold this book!";
            log.error(message);
            throw new LogicException(message);
        }
        book.setUserCard(null);
        bookDAO.saveAndFlush(book);
        log.info(" Book with id = {} was returned to library", bookId);
    }

    @Transactional
    public UserCard completeConversionToEntity(UserCard userCard, UserCardDTO userCardDTO) {
        if (nonNull(userCardDTO.getBooksId())) {
            Set<Book> books = userCardDTO.getBooksId()
                    .stream()
                    .map(bookId -> bookDAO.findById(bookId).orElse(null))
                    .collect(Collectors.toSet());
            userCard.setBooks(books);
        }
        if (nonNull(userCardDTO.getReviewsId())) {
            Set<Review> reviews = userCardDTO.getReviewsId()
                    .stream()
                    .map(reviewId -> reviewDAO.findById(reviewId).orElse(null))
                    .collect(Collectors.toSet());
            userCard.setReviews(reviews);
        }
        if (nonNull(userCardDTO.getUserId())) {
            userCard.setUser(userDAO.findById(userCardDTO.getUserId()).orElse(null));
        }
        return userCard;
    }

    @Transactional
    public UserCardDTO completeConversionToDTO(UserCardDTO userCardDTO, UserCard userCard) {
        if (nonNull(userCard)) {
            if (nonNull(userCard.getBooks())) {
                Set<Integer> books = userCard.getBooks()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(Book::getId)
                        .collect(Collectors.toSet());
                userCardDTO.setBooksId(books);
                Set<String> bookTitles = userCard.getBooks()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(Book::getTitle)
                        .collect(Collectors.toSet());
                userCardDTO.setBooksTitles(bookTitles);
            }
            if (nonNull(userCard.getReviews())) {
                Set<Integer> reviews = userCard.getReviews()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(Review::getId)
                        .collect(Collectors.toSet());
                userCardDTO.setReviewsId(reviews);
            }
            if (nonNull(userCard.getUser())) {
                userCardDTO.setUserId(userCard.getUser().getId());
            }
        }
        return userCardDTO;
    }
}
