package com.netcracker.education.services.converters;

import com.netcracker.education.dao.domain.Author;
import com.netcracker.education.services.dto.AuthorDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthorConverter implements Converter<Author, AuthorDTO> {
    @Override
    public Author convertToEntity(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setId(authorDTO.getId());
        author.setFirstName(authorDTO.getFirstName());
        author.setLastName(authorDTO.getLastName());
        author.setDescription(authorDTO.getDescription());
        author.setCountry(authorDTO.getCountry());
        return author;
    }

    @Override
    public AuthorDTO convertToDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        if (Objects.nonNull(author)) {
            authorDTO = AuthorDTO.builder()
                    .id(author.getId())
                    .firstName(author.getFirstName())
                    .lastName(author.getLastName())
                    .description(author.getDescription())
                    .country(author.getCountry()).build();
        }
        return authorDTO;
    }
}
