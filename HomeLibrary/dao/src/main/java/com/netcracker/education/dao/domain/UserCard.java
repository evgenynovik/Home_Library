package com.netcracker.education.dao.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_cards")
public class UserCard extends PrimaryEntity {

    @Column(columnDefinition = "VARCHAR(100)")
    private String email;

    @Column(columnDefinition = "BOOLEAN default false")
    private Boolean permission;

    @OneToMany(mappedBy="userCard", cascade = CascadeType.ALL)
    private Set<Book> books;

    @OneToMany(mappedBy="userCard", cascade = CascadeType.ALL)
    private Set<Review> reviews;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
