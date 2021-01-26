package com.netcracker.education.services.security;

import com.netcracker.education.dao.domain.User;
import com.netcracker.education.dao.domain.UserCard;
import com.netcracker.education.services.exceptions.LogicException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private String userName;
    private String password;
    private List<GrantedAuthority> authorities;
    private boolean active;
    private User user;

    public CustomUserDetails(User user) {
        this.userName = user.getName();
        this.password = user.getPassword();
        this.authorities = Arrays.stream(user.getRole().split(", "))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        this.user = user;
    }

    public CustomUserDetails() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }

    public Integer getUserId() {
        return this.user.getId();
    }

    public Integer getUserCardId() {
        UserCard userCard = this.user.getUserCard();
        if (userCard == null) {
            throw new LogicException("You do not hold any cards!");
        } else {
            return userCard.getId();
        }
    }
}
