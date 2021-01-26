package com.netcracker.education.controllers.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcracker.education.dao.domain.Author;
import com.netcracker.education.dao.domain.Book;
import com.netcracker.education.dao.interfaces.AuthorDAO;
import com.netcracker.education.dao.interfaces.BookDAO;
import com.netcracker.education.services.dto.AuthorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "", roles = "ADMIN")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthorDAO authorDAO;
    @MockBean
    private BookDAO bookDAO;
    private final AuthorDTO authorDTO = AuthorDTO.builder()
            .id(1)
            .firstName("Jack")
            .lastName("London")
            .description("He was an American writer")
            .country("US").build();

    @BeforeEach
    public void setUp() {
        Author author = new Author("Jack", "London", "He was an American writer",
                "US", Collections.emptySet());
        Book book = new Book();
        Author primaryAuthor = new Author("Jack", "London", "He was an American writer",
                "US", Collections.emptySet());

        author.setId(1);
        book.setId(9);

        when(authorDAO.saveAndFlush(primaryAuthor)).thenReturn(author);
        when(authorDAO.saveAndFlush(author)).thenReturn(author);
        when(authorDAO.findById(1)).thenReturn(Optional.of(author));
        when(authorDAO.findAll()).thenReturn(Collections.singletonList(author));
        when(bookDAO.findById(1)).thenReturn((Optional.of(book)));
        when(authorDAO.findAllByBooksContains(book))
                .thenReturn(Collections.singletonList(author));
    }

    @Test
    public void createTest() {
        AuthorDTO primaryAuthorDTO = AuthorDTO.builder()
                .firstName("Jack")
                .lastName("London")
                .description("He was an American writer")
                .country("US").build();

        assertDoesNotThrow(() ->
                mockMvc.perform(post("/library/v1/authors")
                        .content(new ObjectMapper().writeValueAsString(primaryAuthorDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id", is(1)))
                        .andExpect(jsonPath("$.firstName", is("Jack")))
                        .andExpect(jsonPath("$.lastName", is("London")))
                        .andExpect(jsonPath("$.country", is("US")))
                        .andExpect(jsonPath("$.description", is("He was an American writer"))));

        verify(authorDAO).saveAndFlush(any(Author.class));
    }

    @Test
    public void searchTest() {
        assertDoesNotThrow(() ->
                mockMvc.perform(get("/library/v1/authors/1")
                        .content(new ObjectMapper().writeValueAsString(authorDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().is(200))
                        .andExpect(jsonPath("$.id", is(1)))
                        .andExpect(jsonPath("$.firstName", is("Jack")))
                        .andExpect(jsonPath("$.lastName", is("London")))
                        .andExpect(jsonPath("$.country", is("US")))
                        .andExpect(jsonPath("$.description", is("He was an American writer"))));

        verify(authorDAO, times(2)).findById(1);
    }

    @Test
    public void updateTest() {
        assertDoesNotThrow(() ->
                mockMvc.perform(put("/library/v1/authors")
                        .content(new ObjectMapper().writeValueAsString(authorDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().is(200))
                        .andExpect(jsonPath("$.id", is(1)))
                        .andExpect(jsonPath("$.firstName", is("Jack")))
                        .andExpect(jsonPath("$.lastName", is("London")))
                        .andExpect(jsonPath("$.country", is("US")))
                        .andExpect(jsonPath("$.description", is("He was an American writer"))));

        verify(authorDAO).saveAndFlush(any(Author.class));
    }

    @Test
    public void getAllTest() {
        assertDoesNotThrow(() ->
                mockMvc.perform(get("/library/v1/authors")
                        .content(new ObjectMapper().writeValueAsString(Collections.singletonList(authorDTO)))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().is(200))
                        .andExpect(jsonPath("$..[0].id").value(1))
                        .andExpect(jsonPath("$..[0].firstName").value("Jack"))
                        .andExpect(jsonPath("$..[0].lastName").value("London"))
                        .andExpect(jsonPath("$..[0].country").value("US"))
                        .andExpect(jsonPath("$..[0].description")
                                .value("He was an American writer")));

        verify(authorDAO).findAll();
    }

    @Test
    public void deleteTest() {
        assertDoesNotThrow(() ->
                mockMvc.perform(delete("/library/v1/authors/1")
                        .content(new ObjectMapper().writeValueAsString(authorDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().is(204)));

        verify(authorDAO).deleteById(1);
    }
}
