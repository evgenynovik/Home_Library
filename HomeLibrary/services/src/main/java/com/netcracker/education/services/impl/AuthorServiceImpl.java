package com.netcracker.education.services.impl;

import com.netcracker.education.dao.domain.Author;
import com.netcracker.education.dao.interfaces.AuthorDAO;
import com.netcracker.education.dao.interfaces.BookDAO;
import com.netcracker.education.services.converters.Converter;
import com.netcracker.education.services.dto.AuthorDTO;
import com.netcracker.education.services.exceptions.LogicException;
import com.netcracker.education.services.interfaces.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDAO authorDAO;
    private final Converter<Author, AuthorDTO> converter;
    private final BookDAO bookDAO;

    @Autowired
    public AuthorServiceImpl(AuthorDAO authorDAO, Converter<Author, AuthorDTO> converter, BookDAO bookDAO) {
        this.authorDAO = authorDAO;
        this.converter = converter;
        this.bookDAO = bookDAO;
    }

    @Override
    @Transactional
    public AuthorDTO create(AuthorDTO authorDTO) {
        Author author = authorDAO.saveAndFlush(converter.convertToEntity(authorDTO));
        log.info(" Author with id = {} is created.", author.getId());
        return converter.convertToDTO(author);
    }

    @Override
    public List<AuthorDTO> getAllByBook(Integer bookId) {
        return authorDAO.findAllByBooksContains(bookDAO.findById(bookId).orElse(null)).stream()
                .map(converter::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDTO search(Integer authorId) {
        if (authorDAO.findById(authorId).isEmpty()) {
            throw new LogicException("This author is not exist!");
        } else {
            Author author = authorDAO.findById(authorId).get();
            log.debug(" Author with id = {} is shown.", authorId);
            return converter.convertToDTO(author);
        }
    }

    @Override
    @Transactional
    public AuthorDTO update(AuthorDTO authorDTO) {
        Author author = authorDAO.saveAndFlush(converter.convertToEntity(authorDTO));
        log.info(" Author with id = {} is updated.", author.getId());
        return converter.convertToDTO(author);
    }

    @Override
    public List<AuthorDTO> getAll() {
        log.debug(" All authors are shown.");
        return authorDAO.findAll()
                .stream()
                .map(converter::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer authorId) {
        if (authorDAO.findById(authorId).isPresent()) {
            authorDAO.deleteById(authorId);
            log.info(" Author with id = {} was deleted.", authorId);
        }
    }
}
