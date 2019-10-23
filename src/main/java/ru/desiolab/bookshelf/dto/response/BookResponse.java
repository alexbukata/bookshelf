package ru.desiolab.bookshelf.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class BookResponse {

    @JsonProperty
    private Long id;
    @JsonProperty
    private AuthorResponse author;
    @JsonProperty
    private String name;
}
