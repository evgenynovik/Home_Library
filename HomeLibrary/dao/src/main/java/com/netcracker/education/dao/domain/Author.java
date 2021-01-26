package com.netcracker.education.dao.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authors")
public class Author extends PrimaryEntity {

    @Column(columnDefinition = "VARCHAR(50)", name = "first_name")
    private String firstName;
    @Column(columnDefinition = "VARCHAR(50)", name = "last_name")
    private String lastName;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "VARCHAR(50)")
    private String country;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Book> books;
}
