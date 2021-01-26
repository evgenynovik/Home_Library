package com.netcracker.education.services.impl;

import com.netcracker.education.dao.domain.BookSeries;
import com.netcracker.education.dao.interfaces.BookSeriesDAO;
import com.netcracker.education.services.converters.Converter;
import com.netcracker.education.services.dto.BookSeriesDTO;
import com.netcracker.education.services.exceptions.LogicException;
import com.netcracker.education.services.interfaces.BookSeriesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookSeriesServiceImpl implements BookSeriesService {

    private final Converter<BookSeries, BookSeriesDTO> converter;
    private final BookSeriesDAO bookSeriesDAO;

    @Autowired
    public BookSeriesServiceImpl(BookSeriesDAO bookSeriesDAO, Converter<BookSeries, BookSeriesDTO> converter) {
        this.bookSeriesDAO = bookSeriesDAO;
        this.converter = converter;
    }

    @Override
    @Transactional
    public BookSeriesDTO create(BookSeriesDTO bookSeriesDTO) {
        BookSeries bookSeries = bookSeriesDAO.saveAndFlush(converter.convertToEntity(bookSeriesDTO));
        log.info(" Book series with id = {} is created.", bookSeries.getId());
        return converter.convertToDTO(bookSeries);
    }

    @Override
    public BookSeriesDTO search(Integer bookSeriesId) {
        if (bookSeriesDAO.findById(bookSeriesId).isEmpty()) {
            throw new LogicException("This series is not exist!");
        } else {
            BookSeries bookSeries = bookSeriesDAO.findById(bookSeriesId).get();
            log.debug(" Book series with id = {} is shown.", bookSeriesId);
            return converter.convertToDTO(bookSeries);
        }
    }

    @Override
    @Transactional
    public BookSeriesDTO update(BookSeriesDTO bookSeriesDTO) {
        BookSeries bookSeries = bookSeriesDAO.saveAndFlush(converter.convertToEntity(bookSeriesDTO));
        log.info(" Book series with id = {} is updated.", bookSeries.getId());
        return converter.convertToDTO(bookSeries);
    }

    @Override
    public List<BookSeriesDTO> getAll() {
        log.debug(" All book series are shown.");
        return bookSeriesDAO.findAll()
                .stream()
                .map(converter::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer bookSeriesId) {
        if (bookSeriesDAO.findById(bookSeriesId).isPresent()) {
            bookSeriesDAO.deleteById(bookSeriesId);
            log.info(" Book series with id = {} was deleted.", bookSeriesId);
        }
    }
}
