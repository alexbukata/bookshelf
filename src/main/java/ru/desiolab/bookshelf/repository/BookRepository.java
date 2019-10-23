package ru.desiolab.bookshelf.repository;

import org.springframework.data.repository.CrudRepository;
import ru.desiolab.bookshelf.dto.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
}
