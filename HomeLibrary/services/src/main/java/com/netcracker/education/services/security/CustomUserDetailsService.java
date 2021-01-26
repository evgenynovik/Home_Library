package com.netcracker.education.services.security;

import com.netcracker.education.dao.domain.User;
import com.netcracker.education.dao.interfaces.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;

    @Autowired
    public CustomUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
       Optional<User> user = userDAO.findByName(name);
       user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + name));
       return user.map(CustomUserDetails::new).get();
    }
}
