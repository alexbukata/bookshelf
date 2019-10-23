package ru.desiolab.bookshelf.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class AuthorResponse {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String name;
}
