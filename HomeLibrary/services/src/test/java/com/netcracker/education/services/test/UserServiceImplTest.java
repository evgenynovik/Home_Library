package com.netcracker.education.services.test;

import com.netcracker.education.dao.domain.User;
import com.netcracker.education.dao.domain.UserCard;
import com.netcracker.education.dao.interfaces.UserCardDAO;
import com.netcracker.education.dao.interfaces.UserDAO;
import com.netcracker.education.services.converters.UserConverter;
import com.netcracker.education.services.dto.UserDTO;
import com.netcracker.education.services.exceptions.LogicException;
import com.netcracker.education.services.impl.UserServiceImpl;
import com.netcracker.education.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {UserConverter.class,
        UserServiceImpl.class, BCryptPasswordEncoder.class})
public class UserServiceImplTest {

    @MockBean
    private UserDAO userDAO;
    @MockBean
    private UserCardDAO userCardDAO;
    @Autowired
    private UserService userService;
    private final UserDTO userDTO = UserDTO.builder()
            .id(4)
            .name("Jack")
            .role("ROLE_USER")
            .password("ghf5")
            .userCardId(1).build();
    private final UserDTO secondUserDTO = UserDTO.builder()
            .id(9)
            .name("Ronald")
            .role("ROLE_USER")
            .password("fpf3")
            .userCardId(8).build();

    @BeforeEach
    public void setUp() {
        User user = new User("Jack", "ghf5", "ROLE_USER", null);
        UserCard userCard = new UserCard("sky@gmail.com", false,
                Collections.emptySet(), Collections.emptySet(), user);
        User primaryUser = new User("Jack", "ghf5", "ROLE_USER",
                null);

        user.setId(4);
        userCard.setId(1);
        user.setUserCard(userCard);
        primaryUser.setUserCard(userCard);

        when(userDAO.saveAndFlush(primaryUser)).thenReturn(user);
        when(userDAO.saveAndFlush(user)).thenReturn(user);
        when(userDAO.findById(4)).thenReturn(Optional.of(user));
        when(userDAO.findAll()).thenReturn(Collections.singletonList(user));
        when(userDAO.getBookBorrower(8)).thenReturn(user);
        when(userCardDAO.findById(1)).thenReturn(Optional.of(userCard));
    }

    @Test
    public void createTest() {
        UserDTO primaryUserDTO = UserDTO.builder()
                .name("Jack")
                .role("ROLE_USER")
                .password("ghf5")
                .userCardId(1).build();

        assertEquals(userDTO, userService.create(primaryUserDTO));
        verify(userDAO).saveAndFlush(any(User.class));
    }

    @Test
    public void searchTest() {
        assertEquals(userDTO, userService.search(4));
        assertEquals("This user is not exist!", assertThrows(LogicException.class,
                () -> userService.search(3)).getMessage());
        verify(userDAO, times(2)).findById(4);
        verify(userDAO).findById(3);
    }

    @Test
    public void updateTest() {
        assertEquals(userDTO.getPassword(), userService.update(userDTO).getPassword());
        assertEquals(userDTO.getName(), userService.update(userDTO).getName());
        assertNotEquals(secondUserDTO, userService.update(userDTO));
        verify(userDAO, times(3)).saveAndFlush(any(User.class));
    }

    @Test
    public void getAllTest() {
        assertEquals(Collections.singletonList(userDTO), userService.getAll());
        verify(userDAO).findAll();
    }

    @Test
    public void showBookBorrowerTest() {
        assertEquals(userDTO, userService.showBookBorrower(8));
        assertNotEquals(secondUserDTO, userService.showBookBorrower(8));
        verify(userDAO, times(4)).getBookBorrower(8);
    }

    @Test
    public void deleteTest() {
        userService.delete(4);
        verify(userDAO).deleteById(4);
    }
}
