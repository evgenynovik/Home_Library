package com.netcracker.education.controllers.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcracker.education.dao.domain.BookSeries;
import com.netcracker.education.dao.interfaces.BookSeriesDAO;
import com.netcracker.education.services.dto.BookSeriesDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "", roles = "ADMIN")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookSeriesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookSeriesDAO bookSeriesDAO;
    private final BookSeriesDTO bookSeriesDTO = BookSeriesDTO.builder()
            .id(1)
            .title("The Chronicles of Amber")
            .description("is a series of fantasy novels ").build();

    @BeforeEach
    public void setUp() {
        BookSeries bookSeries = new BookSeries("The Chronicles of Amber",
                "is a series of fantasy novels ", Collections.emptySet());
        bookSeries.setId(1);
        BookSeries primaryBookSeries = new BookSeries("The Chronicles of Amber",
                "is a series of fantasy novels ", Collections.emptySet());

        when(bookSeriesDAO.saveAndFlush(primaryBookSeries)).thenReturn(bookSeries); // creating
        when(bookSeriesDAO.saveAndFlush(bookSeries)).thenReturn(bookSeries); // update
        when(bookSeriesDAO.findById(1)).thenReturn(Optional.of(bookSeries));
        when(bookSeriesDAO.findAll()).thenReturn(Collections.singletonList(bookSeries));
    }

    @Test
    public void createTest() {
        BookSeriesDTO primaryBookSeriesDTO = BookSeriesDTO.builder()
                .title("The Chronicles of Amber")
                .description("is a series of fantasy novels ").build();

        assertDoesNotThrow(() ->
                mockMvc.perform(post("/library/v1/series")
                        .content(new ObjectMapper().writeValueAsString(primaryBookSeriesDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id", is(1)))
                        .andExpect(jsonPath("$.title", is("The Chronicles of Amber")))
                        .andExpect(jsonPath("$.description", is("is a series of fantasy novels "))));

        verify(bookSeriesDAO).saveAndFlush(any(BookSeries.class));
    }

    @Test
    public void searchTest() {
        assertDoesNotThrow(() ->
                mockMvc.perform(get("/library/v1/series/1")
                        .content(new ObjectMapper().writeValueAsString(bookSeriesDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().is(200))
                        .andExpect(jsonPath("$.id", is(1)))
                        .andExpect(jsonPath("$.title", is("The Chronicles of Amber")))
                        .andExpect(jsonPath("$.description", is("is a series of fantasy novels "))));

        verify(bookSeriesDAO, times(2)).findById(1);
    }

    @Test
    public void updateTest() {
        assertDoesNotThrow(() ->
                mockMvc.perform(put("/library/v1/series")
                        .content(new ObjectMapper().writeValueAsString(bookSeriesDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().is(200))
                        .andExpect(jsonPath("$.id", is(1)))
                        .andExpect(jsonPath("$.title", is("The Chronicles of Amber")))
                        .andExpect(jsonPath("$.description", is("is a series of fantasy novels "))));

        verify(bookSeriesDAO).saveAndFlush(any(BookSeries.class));
    }

    @Test
    public void deleteTest() {
        assertDoesNotThrow(() ->
                mockMvc.perform(delete("/library/v1/series/1")
                        .content(new ObjectMapper().writeValueAsString(bookSeriesDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().is(204)));

        verify(bookSeriesDAO).deleteById(1);
    }

    @Test
    public void getAllTest() {
        assertDoesNotThrow(() ->
                mockMvc.perform(get("/library/v1/series")
                        .content(new ObjectMapper().writeValueAsString(Collections.singletonList(bookSeriesDTO)))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().is(200))
                        .andExpect(jsonPath("$..[0].id").value(1))
                        .andExpect(jsonPath("$..[0].title").value("The Chronicles of Amber"))
                        .andExpect(jsonPath("$..[0].description")
                                .value("is a series of fantasy novels ")));

        verify(bookSeriesDAO).findAll();
    }
}
