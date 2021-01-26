package com.netcracker.education.dao.interfaces;

import com.netcracker.education.dao.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreDAO extends JpaRepository<Genre, Integer> {
}
