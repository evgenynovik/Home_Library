package com.netcracker.education.dao.interfaces;

import com.netcracker.education.dao.domain.Author;
import com.netcracker.education.dao.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorDAO extends JpaRepository<Author, Integer> {
    List<Author> findAllByBooksContains(Book book);
}
