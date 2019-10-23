package ru.desiolab.bookshelf.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(fluent = true)
@Entity
@Table(name = "BOOK")
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(targetEntity = Author.class)
    @JoinColumn(name = "authorId", nullable = false)
    private Author author;

    private String name;
}
