package ru.desiolab.bookshelf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.desiolab.bookshelf.dto.Author;
import ru.desiolab.bookshelf.dto.request.AuthorRequest;
import ru.desiolab.bookshelf.dto.response.AuthorResponse;
import ru.desiolab.bookshelf.factory.ResponseFactory;
import ru.desiolab.bookshelf.service.AuthorService;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final ResponseFactory responseFactory;

    @GetMapping(path = "/author/{id}")
    @PreAuthorize("hasRole('USER')")
    public AuthorResponse getAuthorById(@PathVariable Long id) {
        Author author = authorService.getAuthor(id);
        return responseFactory.buildAuthorResponse(author);
    }

    @PostMapping(path = "/author")
    @PreAuthorize("hasRole('ADMIN')")
    public Long createAuthor(@RequestBody AuthorRequest authorRequest) {
        return authorService.createAuthor(authorRequest);
    }

    @PutMapping(path = "/author/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateAuthor(@PathVariable("id") Long authorId,
                             @RequestBody AuthorRequest authorRequest) {
        authorService.updateAuthor(authorId, authorRequest);
    }

    @DeleteMapping(path = "/author/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAuthor(@PathVariable("id") Long authorId) {
        authorService.deleteAuthor(authorId);
    }
}
