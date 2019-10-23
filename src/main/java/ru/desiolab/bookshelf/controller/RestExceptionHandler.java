package ru.desiolab.bookshelf.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.desiolab.bookshelf.error.AuthorNotFoundException;
import ru.desiolab.bookshelf.error.BookNotFoundException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({AuthorNotFoundException.class, BookNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void notFound() {
    }
}
