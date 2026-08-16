package com.saraiva.biblioteca.service;

import com.saraiva.biblioteca.dto.BookRequest;
import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.entity.Book;
import com.saraiva.biblioteca.exception.ResourceNotFoundException;
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
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }


    public Book save(BookRequest bookRequest){
        Integer authorId = bookRequest.getAuthorId();

        Author author = authorRepository.findById(authorId).orElseThrow(
                () -> new ResourceNotFoundException("Author not found")
        );

        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(bookRequest.getTitle());
        book.setPublicationYear(bookRequest.getPublicationYear());
        book.setRead(bookRequest.getRead());

        return bookRepository.save(book);
    }
}
