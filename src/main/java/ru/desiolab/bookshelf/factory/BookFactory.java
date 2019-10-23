package ru.desiolab.bookshelf.factory;

import org.springframework.stereotype.Component;
import ru.desiolab.bookshelf.dto.Author;
import ru.desiolab.bookshelf.dto.Book;

@Component
public class BookFactory {

    public Book createBook(String name, Author author) {
        return new Book()
                .author(author)
                .name(name);
    }

    public Book createBook(Long bookId, String name, Author author) {
        return createBook(name, author)
                .id(bookId);
    }
}
