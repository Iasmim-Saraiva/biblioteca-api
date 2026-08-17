package com.saraiva.biblioteca.mapper;

import com.saraiva.biblioteca.dto.AuthorResponse;
import com.saraiva.biblioteca.entity.Author;

public class AuthorMapper {

    public static AuthorResponse toResponse(Author author){
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setId(author.getId());
        authorResponse.setName(author.getName());

        return authorResponse;
    }
}
