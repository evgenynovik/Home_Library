package com.netcracker.education.services.test;

import com.netcracker.education.dao.domain.Genre;
import com.netcracker.education.dao.interfaces.GenreDAO;
import com.netcracker.education.services.converters.GenreConverter;
import com.netcracker.education.services.dto.GenreDTO;
import com.netcracker.education.services.impl.GenreServiceImpl;
import com.netcracker.education.services.interfaces.GenreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GenreConverter.class,
        GenreServiceImpl.class})
public class GenreServiceImplTest {

    @MockBean
    private GenreDAO genreDAO;
    @Autowired
    private GenreService genreService;
    private final GenreDTO genreDTO = GenreDTO.builder()
            .id(2)
            .title("scientific literature")
            .description("is scholarly publications that report theoretical work in the natural sciences")
            .build();
    private final GenreDTO secondGenreDTO = GenreDTO.builder()
            .id(11)
            .title("horror")
            .description("horror is frequently supernatural")
            .build();

    @BeforeEach
    public void setUp() {
        Genre genre = new Genre("scientific literature",
                "is scholarly publications that report theoretical work in the natural sciences",
                Collections.emptySet());
        Genre primaryGenre = new Genre("scientific literature",
                "is scholarly publications that report theoretical work in the natural sciences",
                Collections.emptySet());

        genre.setId(2);

        when(genreDAO.saveAndFlush(primaryGenre)).thenReturn(genre);
        when(genreDAO.saveAndFlush(genre)).thenReturn(genre);
        when(genreDAO.findById(2)).thenReturn(Optional.of(genre));
        when(genreDAO.findAll()).thenReturn(Collections.singletonList(genre));
    }

    @Test
    public void createTest() {
        GenreDTO primaryGenreDTO = GenreDTO.builder()
                .title("scientific literature")
                .description("is scholarly publications that report theoretical work in the natural sciences")
                .build();

        assertEquals(genreDTO, genreService.create(primaryGenreDTO));
        verify(genreDAO).saveAndFlush(any(Genre.class));
    }

    @Test
    public void searchTest() {
        assertEquals(genreDTO, genreService.search(2));
        assertNotEquals(secondGenreDTO, genreService.search(2));
        verify(genreDAO, times(4)).findById(2);
    }

    @Test
    public void updateTest() {
        assertEquals(genreDTO, genreService.update(genreDTO));
        assertNotEquals(secondGenreDTO, genreService.update(genreDTO));
        verify(genreDAO, times(2)).saveAndFlush(any(Genre.class));
    }

    @Test
    public void deleteTest() {
        genreService.delete(2);
        verify(genreDAO).deleteById(2);
    }

    @Test
    public void getAllTest() {
        assertEquals(Collections.singletonList(genreDTO), genreService.getAll());
        verify(genreDAO).findAll();
    }
}
