package com.netcracker.education.dao.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "genres")
public class Genre extends PrimaryEntity {

    @Column(columnDefinition = "VARCHAR(100)")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy="genre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Book> books;
}
