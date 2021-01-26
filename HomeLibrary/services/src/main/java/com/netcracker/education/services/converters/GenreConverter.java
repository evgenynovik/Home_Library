package com.netcracker.education.services.converters;

import com.netcracker.education.dao.domain.Genre;
import com.netcracker.education.services.dto.GenreDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GenreConverter implements Converter<Genre, GenreDTO> {

    @Override
    public Genre convertToEntity(GenreDTO genreDTO) {
        Genre genre = new Genre();
        genre.setId(genreDTO.getId());
        genre.setTitle(genreDTO.getTitle());
        genre.setDescription(genreDTO.getDescription());
        return genre;
    }

    @Override
    public GenreDTO convertToDTO(Genre genre) {
        GenreDTO genreDTO = new GenreDTO();
        if (Objects.nonNull(genre)) {
            genreDTO = GenreDTO.builder()
                    .id(genre.getId())
                    .title(genre.getTitle())
                    .description(genre.getDescription())
                    .build();
        }
        return genreDTO;
    }
}
