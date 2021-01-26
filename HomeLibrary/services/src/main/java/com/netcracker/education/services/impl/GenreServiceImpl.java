package com.netcracker.education.services.impl;

import com.netcracker.education.dao.domain.Genre;
import com.netcracker.education.dao.interfaces.GenreDAO;
import com.netcracker.education.services.converters.Converter;
import com.netcracker.education.services.dto.GenreDTO;
import com.netcracker.education.services.exceptions.LogicException;
import com.netcracker.education.services.interfaces.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GenreServiceImpl implements GenreService {

    private final Converter<Genre, GenreDTO> converter;
    private final GenreDAO genreDAO;

    @Autowired
    public GenreServiceImpl(Converter<Genre, GenreDTO> converter, GenreDAO genreDAO) {
        this.converter = converter;
        this.genreDAO = genreDAO;
    }

    @Override
    @Transactional
    public GenreDTO create(GenreDTO genreDTO) {
        Genre genre = genreDAO.saveAndFlush(converter.convertToEntity(genreDTO));
        log.info(" Genre with id = {} is created.", genre.getId());
        return converter.convertToDTO(genre);
    }

    @Override
    public GenreDTO search(Integer genreId) {
        if (genreDAO.findById(genreId).isEmpty()) {
            throw new LogicException("This genre is not exist!");
        } else {
            Genre genre = genreDAO.findById(genreId).get();
            log.debug(" Genre with id = {} is shown.", genreId);
            return converter.convertToDTO(genre);
        }
    }

    @Override
    @Transactional
    public GenreDTO update(GenreDTO genreDTO) {
        Genre genre = genreDAO.saveAndFlush(converter.convertToEntity(genreDTO));
        log.info(" Genre with id = {} is updated.", genre.getId());
        return converter.convertToDTO(genre);
    }

    @Override
    public void delete(Integer genreId) {
        if (genreDAO.findById(genreId).isPresent()) {
            genreDAO.deleteById(genreId);
            log.info(" Genre with id = {} was deleted.", genreId);
        }
    }

    @Override
    public List<GenreDTO> getAll() {
        log.debug(" All genres are shown.");
        return genreDAO.findAll()
                .stream()
                .map(converter::convertToDTO)
                .collect(Collectors.toList());
    }
}
