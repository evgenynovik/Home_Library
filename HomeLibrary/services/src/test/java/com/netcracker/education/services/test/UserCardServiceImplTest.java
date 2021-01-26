package com.netcracker.education.services.test;

import com.netcracker.education.dao.domain.Book;
import com.netcracker.education.dao.domain.User;
import com.netcracker.education.dao.domain.UserCard;
import com.netcracker.education.dao.interfaces.BookDAO;
import com.netcracker.education.dao.interfaces.ReviewDAO;
import com.netcracker.education.dao.interfaces.UserCardDAO;
import com.netcracker.education.dao.interfaces.UserDAO;
import com.netcracker.education.services.converters.UserCardConverter;
import com.netcracker.education.services.dto.UserCardDTO;
import com.netcracker.education.services.exceptions.LogicException;
import com.netcracker.education.services.impl.UserCardServiceImpl;
import com.netcracker.education.services.interfaces.UserCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {UserCardConverter.class,
        UserCardServiceImpl.class})
@ActiveProfiles("test")
public class UserCardServiceImplTest {

    @Autowired
    private UserCardService userCardService;
    @MockBean
    private UserCardDAO userCardDAO;
    @MockBean
    private BookDAO bookDAO;
    @MockBean
    private ReviewDAO reviewDAO;
    @MockBean
    private UserDAO userDAO;
    @Value("${books.limit}")
    private Integer booksLimit;
    private final UserCardDTO userCardDTO = UserCardDTO.builder()
            .id(6)
            .email("sky@gmail.com")
            .userId(7)
            .permission(false).build();
    private final UserCardDTO secondUserCardDTO = UserCardDTO.builder()
            .email("forest@gmail.com")
            .userId(9)
            .permission(true).build();
    private final Book book = new Book("The hobbit", 1954, "Adventure book",
            null, null, null, null, null);
    private final UserCard userCard = new UserCard("sky@gmail.com", false,
            new HashSet<>(), new HashSet<>(), null);

    @BeforeEach
    public void setUp() {
        User user = new User("Lui", "bhgvyff", "user", userCard);
        UserCard primaryUserCard = new UserCard("sky@gmail.com", false,
                new HashSet<>(), new HashSet<>(), null);

        userCard.setId(6);
        user.setId(7);
        userCard.setUser(user);
        book.setId(7);
        primaryUserCard.setUser(user);
        userCard.setPermission(true);

        when(userCardDAO.saveAndFlush(primaryUserCard)).thenReturn(userCard);
        when(userCardDAO.saveAndFlush(userCard)).thenReturn(userCard);
        when(userDAO.saveAndFlush(user)).thenReturn(user);
        when(userDAO.findById(7)).thenReturn(Optional.of(user));
        when(userCardDAO.findById(6)).thenReturn(Optional.of(userCard));
        when(userCardDAO.findAll()).thenReturn(Collections.singletonList(userCard));
        when(bookDAO.findById(7)).thenReturn(Optional.of(book));
        when(userCardDAO.permit(6)).thenReturn(userCard);

        userCard.setPermission(false);
    }

    @Test
    public void createTest() {
        UserCardDTO primaryUserCardDTO = UserCardDTO.builder()
                .email("sky@gmail.com")
                .userId(7)
                .permission(false)
                .build();

        assertEquals("You already have a personal card!", assertThrows(LogicException.class, () ->
                assertEquals(userCardDTO, userCardService.create(primaryUserCardDTO))).getMessage());
    }

    @Test
    public void searchTest() {
        assertEquals(userCardDTO, userCardService.search(6));
        verify(userCardDAO, times(2)).findById(6);
    }

    @Test
    public void updateTest() {
        assertEquals(userCardDTO, userCardService.update(userCardDTO));
        assertNotEquals(secondUserCardDTO, userCardService.update(userCardDTO));
        verify(userCardDAO, times(2)).saveAndFlush(any(UserCard.class));
    }

    @Test
    public void getAllTest() {
        assertEquals(Collections.singletonList(userCardDTO), userCardService.getAll());
        verify(userCardDAO).findAll();
    }

    @Test
    public void addBookToUserCardTest() {
        Set<Integer> bookSet = userCardDTO.getBooksId();
        Set<String> titles = new HashSet<>();
        Book first = new Book();

        bookSet.add(7);
        userCardDTO.setBooksId(bookSet);
        titles.add("The hobbit");
        userCardDTO.setBooksTitles(titles);

        assertDoesNotThrow(() -> assertEquals(userCardDTO, userCardService.addBookToUserCard(6, 7)));
        assertEquals(" This book or this user card is not exist.", assertThrows(LogicException.class, () ->
                assertNotEquals(userCardDTO, userCardService.addBookToUserCard(6, 8))).getMessage());
        assertEquals(" This book is occupied by another person.",
                assertThrows(LogicException.class, () ->
                        userCardService.addBookToUserCard(6, 7)).getMessage());

        first.setId(1);
        when(bookDAO.findById(1)).thenReturn(Optional.of(first));

        assertEquals(String.format(" You can't hold more than %s books at a time", booksLimit),
                assertThrows(LogicException.class, () ->
                        userCardService.addBookToUserCard(6, 1)).getMessage());
    }

    @Test
    public void permitTest() {
        userCardDTO.setPermission(true);
        userCard.setPermission(true);

        assertEquals(userCardDTO, userCardService.permit(6));
        assertNull(userCardService.permit(20));
        verify(userCardDAO).permit(6);
    }

    @Test
    public void returnBookToLibraryTest() {
        Set<Book> books = userCard.getBooks();
        books.add(book);
        userCard.setBooks(books);

        assertDoesNotThrow(() -> userCardService.returnBookToLibrary(6, 7));

        books.remove(book);
        userCard.setBooks(books);

        assertEquals(" You don't hold this book!", assertThrows(LogicException.class, () ->
                userCardService.returnBookToLibrary(6, 7)).getMessage());
    }

    @Test
    public void deleteTest() {
        userCardService.delete(6);
        verify(userCardDAO).deleteById(6);
    }
}
