package com.netcracker.education.services.interfaces;

import com.netcracker.education.services.dto.UserDTO;
import com.netcracker.education.services.exceptions.LogicException;

import java.util.List;

public interface UserService {

    UserDTO create(UserDTO userDTO);

    UserDTO search(Integer userId);

    UserDTO update(UserDTO userDTO);

    UserDTO showBookBorrower(Integer bookId);

    void delete(Integer userId);

    List<UserDTO> getAll();
}
