package com.netcracker.education.services.impl;

import com.netcracker.education.dao.domain.User;
import com.netcracker.education.dao.interfaces.UserCardDAO;
import com.netcracker.education.dao.interfaces.UserDAO;
import com.netcracker.education.services.converters.Converter;
import com.netcracker.education.services.dto.UserDTO;
import com.netcracker.education.services.exceptions.LogicException;
import com.netcracker.education.services.interfaces.UserService;
import com.netcracker.education.services.security.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserCardDAO userCardDAO;
    private final UserDAO userDAO;
    private final Converter<User, UserDTO> converter;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserCardDAO userCardDAO, UserDAO userDAO, Converter<User,
            UserDTO> converter, PasswordEncoder passwordEncoder) {
        this.userCardDAO = userCardDAO;
        this.userDAO = userDAO;
        this.converter = converter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDTO create(UserDTO userDTO) {
        userDTO.setRole(Role.ROLE_USER.name());
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setUserCardId(null);
        User user = userDAO.saveAndFlush(completeConversionToEntity(converter.convertToEntity(userDTO), userDTO));
        log.info(" User with id = {} is created.", user.getId());
        return completeConversionToDTO(converter.convertToDTO(user), user);
    }

    @Override
    public UserDTO search(Integer userId) {
        if (userDAO.findById(userId).isEmpty()) {
            throw new LogicException("This user is not exist!");
        } else {
            User user = userDAO.findById(userId).get();
            log.debug(" User with id = {} is shown.", userId);
            return completeConversionToDTO(converter.convertToDTO(user), user);
        }
    }

    @Override
    public void delete(Integer userId) {
        if (userDAO.findById(userId).isPresent()) {
            userDAO.deleteById(userId);
            log.info(" User with id = {} was deleted.", userId);
        }
    }

    @Override
    @Transactional
    public UserDTO update(UserDTO userDTO) {
        userDTO.setRole(Role.ROLE_USER.name());
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userDAO.saveAndFlush(completeConversionToEntity(converter.convertToEntity(userDTO), userDTO));
        log.info(" User with id = {} was updated.", user.getId());
        return completeConversionToDTO(converter.convertToDTO(user), user);
    }

    @Override
    @Transactional
    public UserDTO showBookBorrower(Integer bookId) {
        if (Objects.isNull(userDAO.getBookBorrower(bookId))) {
            throw new LogicException("User or book is not exist or the book is free to take.");
        } else {
            User user = userDAO.getBookBorrower(bookId);
            log.debug(" Person who borrows book with id = {} is shown.", bookId);
            return completeConversionToDTO(converter.convertToDTO(user), user);
        }
    }

    @Override
    public List<UserDTO> getAll() {
        log.debug(" All users are shown.");
        return userDAO.findAll()
                .stream()
                .map(user -> completeConversionToDTO(converter.convertToDTO(user), user))
                .collect(Collectors.toList());
    }

    @Transactional
    public User completeConversionToEntity(User user, UserDTO userDTO) {
        if (nonNull(userDTO.getUserCardId())) {
            user.setUserCard(userCardDAO.findById(userDTO.getUserCardId()).orElse(null));
        }
        return user;
    }

    @Transactional
    public UserDTO completeConversionToDTO(UserDTO userDTO, User user) {
        if (nonNull(user) && nonNull(user.getUserCard())) {
            userDTO.setUserCardId(user.getUserCard().getId());
        }
        return userDTO;
    }
}
