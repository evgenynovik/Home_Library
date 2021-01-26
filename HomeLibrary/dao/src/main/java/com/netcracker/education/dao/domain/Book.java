package com.netcracker.education.dao.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@ToString
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book extends PrimaryEntity {

    @Column(columnDefinition = "VARCHAR(100)")
    private String title;
    @Column(columnDefinition = "INTEGER", name = "book_year")
    private Integer year;
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_card_id")
    private UserCard userCard;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "book_authors", joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id",
            nullable = false), inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id",
            nullable = false))
    private Set<Author> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviews;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_series_id")
    private BookSeries bookSeries;
}
