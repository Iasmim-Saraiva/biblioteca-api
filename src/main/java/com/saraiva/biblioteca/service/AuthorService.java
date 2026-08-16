package com.saraiva.biblioteca.service;

import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> findAll(){
        return authorRepository.findAll();
    }

    public Author findById(Integer id){
        return authorRepository.findById(id).orElse(null);
    }

    public Author save(Author author){
        return authorRepository.save(author);
    }
}
