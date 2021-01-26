package com.netcracker.education.services.interfaces;

import com.netcracker.education.services.dto.BookSeriesDTO;
import com.netcracker.education.services.exceptions.LogicException;

import java.util.List;

public interface BookSeriesService {

    BookSeriesDTO create(BookSeriesDTO bookSeriesDTO);

    BookSeriesDTO search(Integer bookSeriesId);

    BookSeriesDTO update(BookSeriesDTO bookSeriesDTO);

    List<BookSeriesDTO> getAll();

    void delete(Integer bookSeriesId);
}
