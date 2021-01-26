package com.netcracker.education.services.test;

import com.netcracker.education.dao.domain.Author;
import com.netcracker.education.dao.domain.Book;
import com.netcracker.education.dao.interfaces.AuthorDAO;
import com.netcracker.education.dao.interfaces.BookDAO;
import com.netcracker.education.services.converters.AuthorConverter;
import com.netcracker.education.services.dto.AuthorDTO;
import com.netcracker.education.services.exceptions.LogicException;
import com.netcracker.education.services.impl.AuthorServiceImpl;
import com.netcracker.education.services.interfaces.AuthorService;
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
@SpringBootTest(classes = {AuthorConverter.class,
        AuthorServiceImpl.class})
@ActiveProfiles("test")
public class AuthorServiceImplTest {

    @MockBean
    private AuthorDAO authorDAO;
    @MockBean
    private BookDAO bookDAO;
    @Autowired
    private AuthorService authorService;
    private final AuthorDTO authorDTO = AuthorDTO.builder()
            .id(1)
            .firstName("Jack")
            .lastName("London")
            .description("He was an American writer")
            .country("US").build();
    private final AuthorDTO secondAuthorDTO = AuthorDTO.builder()
            .id(7)
            .firstName("Edgar")
            .lastName("Poe")
            .description("He was an American writer")
            .country("US").build();

    @BeforeEach
    public void setUp() {
        Author author = new Author("Jack", "London", "He was an American writer",
                "US", Collections.emptySet());
        Author primaryAuthor = new Author("Jack", "London", "He was an American writer",
                "US", Collections.emptySet());
        Book book = new Book();

        author.setId(1);
        book.setId(9);

        when(authorDAO.saveAndFlush(primaryAuthor)).thenReturn(author);
        when(authorDAO.saveAndFlush(author)).thenReturn(author);
        when(bookDAO.findById(1)).thenReturn((Optional.of(book)));
        when(authorDAO.findAllByBooksContains(book)).thenReturn(Collections.singletonList(author));
        when(authorDAO.findById(1)).thenReturn(Optional.of(author));
        when(authorDAO.findAll()).thenReturn(Collections.singletonList(author));
    }

    @Test
    public void createTest() {
        AuthorDTO primaryAuthorDTO = AuthorDTO.builder()
                .firstName("Jack")
                .lastName("London")
                .description("He was an American writer")
                .country("US").build();

        assertEquals(authorDTO, authorService.create(primaryAuthorDTO));
        verify(authorDAO).saveAndFlush(any(Author.class));
    }

    @Test
    public void getAllByBookTest() {
        assertEquals(Collections.singletonList(authorDTO), authorService.getAllByBook(1));
        assertNotEquals(Collections.singletonList(secondAuthorDTO), authorService.getAllByBook(1));
        verify(authorDAO, times(2)).findAllByBooksContains(any(Book.class));
    }

    @Test
    public void searchTest() {
        assertEquals(authorDTO, authorService.search(1));
        assertEquals("This author is not exist!", assertThrows(LogicException.class,
                () -> authorService.search(3)).getMessage());
        verify(authorDAO, times(3)).findById(anyInt());
    }

    @Test
    public void updateTest() {
        assertEquals(authorDTO, authorService.update(authorDTO));
        assertNotEquals(secondAuthorDTO, authorService.update(authorDTO));
        verify(authorDAO, times(2)).saveAndFlush(any(Author.class));
    }

    @Test
    public void getAllTest() {
        assertEquals(Collections.singletonList(authorDTO), authorService.getAll());
        verify(authorDAO).findAll();
    }

    @Test
    public void deleteTest() {
        authorService.delete(1);
        verify(authorDAO).deleteById(1);
    }
}
