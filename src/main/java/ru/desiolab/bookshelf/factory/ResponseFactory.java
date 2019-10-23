package ru.desiolab.bookshelf.factory;

import org.springframework.stereotype.Component;
import ru.desiolab.bookshelf.dto.Author;
import ru.desiolab.bookshelf.dto.Book;
import ru.desiolab.bookshelf.dto.response.AuthorResponse;
import ru.desiolab.bookshelf.dto.response.BookResponse;

@Component
public class ResponseFactory {

    public AuthorResponse buildAuthorResponse(Author author) {
        return new AuthorResponse()
                .id(author.id())
                .name(author.name());
    }

    public BookResponse buildBookResponse(Book book) {
        return new BookResponse()
                .id(book.id())
                .author(buildAuthorResponse(book.author()))
                .name(book.name());
    }
}
