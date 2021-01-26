package com.netcracker.education.services.converters;

import com.netcracker.education.dao.domain.User;
import com.netcracker.education.services.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserConverter implements Converter<User, UserDTO> {

    @Override
    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        return user;
    }

    @Override
    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        if (Objects.nonNull(user)) {
            userDTO = UserDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .password(user.getPassword())
                    .role(user.getRole()).build();
        }
        return userDTO;
    }
}
