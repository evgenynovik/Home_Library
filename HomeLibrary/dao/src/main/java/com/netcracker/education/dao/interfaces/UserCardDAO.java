package com.netcracker.education.dao.interfaces;

import com.netcracker.education.dao.domain.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCardDAO extends JpaRepository<UserCard, Integer> {
    @Query(value = "UPDATE user_cards uc SET permission = true WHERE id = ?1 RETURNING " +
            "id, email, phone_number, user_id, permission;", nativeQuery = true)
    UserCard permit(Integer userCardId);
}
