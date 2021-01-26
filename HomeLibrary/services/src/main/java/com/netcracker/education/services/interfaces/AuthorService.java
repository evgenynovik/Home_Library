package com.netcracker.education.services.interfaces;

import com.netcracker.education.services.dto.AuthorDTO;
import com.netcracker.education.services.exceptions.LogicException;

import java.util.List;

public interface AuthorService {

    AuthorDTO create(AuthorDTO authorDTO);

    AuthorDTO search(Integer authorId);

    AuthorDTO update(AuthorDTO authorDTO);

    List<AuthorDTO> getAll();

    void delete(Integer authorId);

    List<AuthorDTO> getAllByBook(Integer bookId);
}
