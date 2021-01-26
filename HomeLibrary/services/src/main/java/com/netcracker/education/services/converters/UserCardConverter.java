package com.netcracker.education.services.converters;

import com.netcracker.education.dao.domain.UserCard;
import com.netcracker.education.services.dto.UserCardDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserCardConverter implements Converter<UserCard, UserCardDTO> {

    @Override
    public UserCard convertToEntity(UserCardDTO userCardDTO) {
        UserCard userCard = new UserCard();
        userCard.setId(userCardDTO.getId());
        userCard.setEmail(userCardDTO.getEmail());
        userCard.setPermission(userCardDTO.getPermission());
        return userCard;
    }

    @Override
    public UserCardDTO convertToDTO(UserCard userCard) {
        UserCardDTO userCardDTO = new UserCardDTO();
        if (Objects.nonNull(userCard)) {
            userCardDTO = UserCardDTO.builder()
                    .id(userCard.getId())
                    .email(userCard.getEmail())
                    .permission(userCard.getPermission()).build();
        }
        return userCardDTO;
    }
}
