package ru.desiolab.bookshelf.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class AuthorRequest {

    @JsonProperty
    private String name;
}
