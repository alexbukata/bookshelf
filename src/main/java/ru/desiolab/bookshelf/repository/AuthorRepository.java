package ru.desiolab.bookshelf.repository;

import org.springframework.data.repository.CrudRepository;
import ru.desiolab.bookshelf.dto.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
