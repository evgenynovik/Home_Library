package com.netcracker.education.dao.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Review extends PrimaryEntity {

    @Column(columnDefinition = "TEXT", name = "review_content")
    private String content;
    @Column(columnDefinition = "SMALLINT check (rating > 0 and rating < 11)")
    private int rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="book_id")
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_card_id")
    private UserCard userCard;
}
