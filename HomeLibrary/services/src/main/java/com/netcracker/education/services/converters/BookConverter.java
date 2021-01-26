package com.netcracker.education.services.converters;

import com.netcracker.education.dao.domain.Book;
import com.netcracker.education.services.dto.BookDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BookConverter implements Converter<Book, BookDTO> {

    @Override
    public Book convertToEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setYear(bookDTO.getYear());
        book.setDescription(bookDTO.getDescription());
        return book;
    }

    @Override
    public BookDTO convertToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        if (Objects.nonNull(book)) {
            bookDTO = BookDTO.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .year(book.getYear())
                    .description(book.getDescription()).build();
        }
        return bookDTO;
    }
}
