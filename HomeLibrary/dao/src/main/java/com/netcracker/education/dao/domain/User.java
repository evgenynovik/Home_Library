package com.netcracker.education.dao.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends PrimaryEntity {

    @Column(columnDefinition = "VARCHAR(100)", name = "user_name")
    private String name;
    @Column(columnDefinition = "VARCHAR(100)", name = "user_password")
    private String password;
    @Column(columnDefinition = "VARCHAR(28)", name = "user_role")
    private String role;
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private UserCard userCard;
}
