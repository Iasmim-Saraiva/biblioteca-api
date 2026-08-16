package com.saraiva.biblioteca.service;

import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.entity.Book;
import com.saraiva.biblioteca.repository.AuthorRepository;
import com.saraiva.biblioteca.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public Book findById(Integer id){
        return bookRepository.findById(id).orElse(null);
    }

    public Book save(Book book){
        Integer authorId = book.getAuthor().getId();
        Author author = authorRepository.findById(authorId).orElse(null);
        book.setAuthor(author);
        return bookRepository.save(book);
    }
}
