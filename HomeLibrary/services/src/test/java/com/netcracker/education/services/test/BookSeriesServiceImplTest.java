package com.netcracker.education.services.test;

import com.netcracker.education.dao.domain.BookSeries;
import com.netcracker.education.dao.interfaces.BookSeriesDAO;
import com.netcracker.education.services.converters.BookSeriesConverter;
import com.netcracker.education.services.dto.BookSeriesDTO;
import com.netcracker.education.services.exceptions.LogicException;
import com.netcracker.education.services.impl.BookSeriesServiceImpl;
import com.netcracker.education.services.interfaces.BookSeriesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BookSeriesConverter.class,
        BookSeriesServiceImpl.class})
public class BookSeriesServiceImplTest {

    @MockBean
    private BookSeriesDAO bookSeriesDAO;
    @Autowired
    private BookSeriesService bookSeriesService;
    private final BookSeriesDTO bookSeriesDTO = BookSeriesDTO.builder()
            .id(1)
            .title("The Chronicles of Amber")
            .description("is a series of fantasy novels ").build();
    private final BookSeriesDTO secondBookSeriesDTO = BookSeriesDTO.builder()
            .id(5)
            .title("Red Rising Trilogy")
            .description("Space opera about saving the world ").build();

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

        assertEquals(bookSeriesDTO, bookSeriesService.create(primaryBookSeriesDTO));
        verify(bookSeriesDAO).saveAndFlush(any(BookSeries.class));
    }

    @Test
    public void searchTest() {
        assertEquals(bookSeriesDTO, bookSeriesService.search(1));
        assertNotEquals(secondBookSeriesDTO, bookSeriesService.search(1));
        assertEquals("This series is not exist!", assertThrows(LogicException.class,
                () -> bookSeriesService.search(3)).getMessage());
        verify(bookSeriesDAO, times(5)).findById(anyInt());
    }

    @Test
    public void updateTest() {
        assertEquals(bookSeriesDTO, bookSeriesService.update(bookSeriesDTO));
        assertNotEquals(secondBookSeriesDTO, bookSeriesService.update(bookSeriesDTO));
        verify(bookSeriesDAO, times(2)).saveAndFlush(any(BookSeries.class));
    }

    @Test
    public void getAllTest() {
        assertEquals(Collections.singletonList(bookSeriesDTO), bookSeriesService.getAll());
        verify(bookSeriesDAO).findAll();
    }

    @Test
    public void deleteTest() {
        bookSeriesService.delete(1);
        verify(bookSeriesDAO).deleteById(1);
    }
}
