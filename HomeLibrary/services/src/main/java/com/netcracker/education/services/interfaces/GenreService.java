package com.netcracker.education.services.interfaces;

import com.netcracker.education.services.dto.GenreDTO;
import com.netcracker.education.services.exceptions.LogicException;

import java.util.List;

public interface GenreService {

    GenreDTO create(GenreDTO genreDTO);

    GenreDTO search(Integer genreId);

    GenreDTO update(GenreDTO genreDTO);

    void delete(Integer genreId);

    List<GenreDTO> getAll();
}
