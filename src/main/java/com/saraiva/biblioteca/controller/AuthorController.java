package com.saraiva.biblioteca.controller;

import com.saraiva.biblioteca.dto.AuthorRequest;
import com.saraiva.biblioteca.dto.AuthorResponse;
import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.mapper.AuthorMapper;
import com.saraiva.biblioteca.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorResponse> findAll(){
        List<Author> authors = authorService.findAll();
        List<AuthorResponse> responses = new ArrayList<>();

        for(Author author : authors){
            responses.add(AuthorMapper.toResponse(author));
        }

        return responses;
    }

    @GetMapping("/{id}")
    public AuthorResponse findById(@PathVariable Integer id){
        Author author = authorService.findById(id);
        return AuthorMapper.toResponse(author);
    }

    @PostMapping
    public ResponseEntity<AuthorResponse> save(@Valid @RequestBody AuthorRequest authorRequest){
        Author author = authorService.save(authorRequest);
        AuthorResponse response = AuthorMapper.toResponse(author);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        authorService.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
