package com.netcracker.education.dao.domain;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" })
@MappedSuperclass
public class PrimaryEntity {
    @Id
    @Column(columnDefinition = "INTEGER", name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
}
