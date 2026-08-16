package com.saraiva.biblioteca.controller;

import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.service.AuthorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<Author> findAll(){
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public Author findById(@PathVariable Integer id){
        return authorService.findById(id);
    }

    @PostMapping
    public Author save(@RequestBody Author author){
        return authorService.save(author);
    }
}
