package ru.desiolab.bookshelf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.desiolab.bookshelf.dto.Book;
import ru.desiolab.bookshelf.dto.request.BookRequest;
import ru.desiolab.bookshelf.dto.response.BookResponse;
import ru.desiolab.bookshelf.factory.ResponseFactory;
import ru.desiolab.bookshelf.service.BookService;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ResponseFactory responseFactory;

    @GetMapping(path = "/book/{id}")
    @PreAuthorize("hasRole('USER')")
    public BookResponse getBookById(@PathVariable Long id) {
        Book book = bookService.getBook(id);
        return responseFactory.buildBookResponse(book);
    }

    @PostMapping(path = "/book")
    @PreAuthorize("hasRole('ADMIN')")
    public Long createBook(@RequestBody BookRequest bookRequest) {
        return bookService.createBook(bookRequest);
    }

    @PutMapping(path = "/book/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateBook(@PathVariable("id") Long bookId,
                           @RequestBody BookRequest bookRequest) {
        bookService.updateBook(bookId, bookRequest);
    }

    @DeleteMapping(path = "/book/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBook(@PathVariable("id") Long bookId) {
        bookService.deleteBook(bookId);
    }
}
