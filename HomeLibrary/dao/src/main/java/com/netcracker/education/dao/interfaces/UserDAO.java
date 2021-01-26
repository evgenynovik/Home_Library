package com.netcracker.education.dao.interfaces;

import com.netcracker.education.dao.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserDAO extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u INNER JOIN u.userCard uc INNER JOIN uc.books b WHERE b.id = ?1")
    User getBookBorrower(Integer bookId);

    Optional<User> findByName(String name);
}
