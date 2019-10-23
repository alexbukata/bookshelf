package ru.desiolab.bookshelf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.desiolab.bookshelf.dto.Author;
import ru.desiolab.bookshelf.dto.request.AuthorRequest;
import ru.desiolab.bookshelf.error.AuthorNotFoundException;
import ru.desiolab.bookshelf.repository.AuthorRepository;

@Component
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    private void validateExists(Long id) {
        boolean exists = authorRepository.existsById(id);
        if (!exists) {
            throw new AuthorNotFoundException("Author with id=" + id + " was not found");
        }
    }

    public Author getAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author with id " + id + " was not found"));
    }

    public Long createAuthor(AuthorRequest authorRequest) {
        Author author = authorRepository.save(new Author().name(authorRequest.name()));
        return author.id();
    }

    public void updateAuthor(Long authorId, AuthorRequest authorRequest) {
        validateExists(authorId);
        Author updatedAuthor = getAuthor(authorId)
                .name(authorRequest.name());
        authorRepository.save(updatedAuthor);
    }

    public void deleteAuthor(Long authorId) {
        validateExists(authorId);
        authorRepository.deleteById(authorId);
    }
}
