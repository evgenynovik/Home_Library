package com.netcracker.education.services.interfaces;

import com.netcracker.education.services.dto.UserCardDTO;
import com.netcracker.education.services.exceptions.LogicException;

import java.util.List;

public interface UserCardService {

    UserCardDTO create(UserCardDTO userCardDTO) throws LogicException;

    UserCardDTO search(Integer userCardId);

    UserCardDTO update(UserCardDTO userCardDTO);

    List<UserCardDTO> getAll();

    void delete(Integer userCardId);

    UserCardDTO addBookToUserCard (Integer userCardId, Integer bookId) throws LogicException;

    void returnBookToLibrary (Integer userCardId, Integer bookId) throws LogicException;

    UserCardDTO permit(Integer userCardId);
}
