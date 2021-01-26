package com.netcracker.education.services.converters;

import com.netcracker.education.dao.domain.BookSeries;
import com.netcracker.education.services.dto.BookSeriesDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BookSeriesConverter implements Converter<BookSeries, BookSeriesDTO> {

    @Override
    public BookSeries convertToEntity(BookSeriesDTO bookSeriesDTO) {
        BookSeries bookSeries = new BookSeries();
        bookSeries.setId(bookSeriesDTO.getId());
        bookSeries.setTitle(bookSeriesDTO.getTitle());
        bookSeries.setDescription(bookSeriesDTO.getDescription());
        return bookSeries;
    }

    @Override
    public BookSeriesDTO convertToDTO(BookSeries bookSeries) {
        BookSeriesDTO bookSeriesDTO = new BookSeriesDTO();
        if (Objects.nonNull(bookSeries)) {
            bookSeriesDTO = BookSeriesDTO.builder()
                    .id(bookSeries.getId())
                    .title(bookSeries.getTitle())
                    .description(bookSeries.getDescription())
                    .build();
        }
        return bookSeriesDTO;
    }
}
