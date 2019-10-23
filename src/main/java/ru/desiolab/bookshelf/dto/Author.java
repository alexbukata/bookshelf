package ru.desiolab.bookshelf.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Accessors(fluent = true)
@Entity
@Table(name = "AUTHOR")
public class Author {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
