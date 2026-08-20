package com.saraiva.biblioteca.service;

import com.saraiva.biblioteca.dto.AuthorRequest;
import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.entity.Book;
import com.saraiva.biblioteca.exception.ResourceConflictException;
import com.saraiva.biblioteca.exception.ResourceNotFoundException;
import com.saraiva.biblioteca.repository.AuthorRepository;
import com.saraiva.biblioteca.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<Author> findAll(){
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Author findById(Integer id){
        return authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author not found"));
    }

    @Transactional
    public Author save(AuthorRequest authorRequest){
        Author author = new Author();
        author.setName(authorRequest.getName());

        return authorRepository.save(author);
    }

    @Transactional
    public void delete(Integer id){
        Author author = findById(id);
        List<Book> books = bookRepository.findByAuthorId(id);
        if (!books.isEmpty()){
            throw new ResourceConflictException("Author cannot be deleted because it has associated books");
        }
        authorRepository.delete(author);
    }
}
