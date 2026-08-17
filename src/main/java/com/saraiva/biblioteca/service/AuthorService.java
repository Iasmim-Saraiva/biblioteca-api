package com.saraiva.biblioteca.service;

import com.saraiva.biblioteca.dto.AuthorRequest;
import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.exception.ResourceNotFoundException;
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
        return authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author not found"));
    }

    public Author save(AuthorRequest authorRequest){
        Author author = new Author();
        author.setName(authorRequest.getName());

        return authorRepository.save(author);
    }

    public void delete(Integer id){
        Author author = findById(id);
        authorRepository.delete(author);
    }
}
