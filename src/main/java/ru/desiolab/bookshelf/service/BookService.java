package ru.desiolab.bookshelf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.desiolab.bookshelf.dto.Author;
import ru.desiolab.bookshelf.dto.Book;
import ru.desiolab.bookshelf.dto.request.BookRequest;
import ru.desiolab.bookshelf.error.BookNotFoundException;
import ru.desiolab.bookshelf.factory.BookFactory;
import ru.desiolab.bookshelf.repository.BookRepository;

@Component
@RequiredArgsConstructor
public class BookService {

    private final AuthorService authorService;
    private final BookRepository bookRepository;
    private final BookFactory bookFactory;


    public Long createBook(BookRequest request) {
        Author author = authorService.getAuthor(request.authorId());
        Book toSave = bookFactory.createBook(request.name(), author);
        Book result = bookRepository.save(toSave);
        return result.id();
    }

    private void validateExists(Long id) {
        boolean exists = bookRepository.existsById(id);
        if (!exists) {
            throw new BookNotFoundException("Book with id=" + id + " was not found");
        }
    }

    public void updateBook(Long bookId, BookRequest request) {
        validateExists(bookId);
        Author author = authorService.getAuthor(request.authorId());
        Book toSave = bookFactory.createBook(bookId, request.name(), author);
        bookRepository.save(toSave);
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id=" + id + " was not found"));
    }

    public void deleteBook(Long bookId) {
        validateExists(bookId);
        bookRepository.deleteById(bookId);
    }
}
