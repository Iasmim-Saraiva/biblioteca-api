package com.saraiva.biblioteca.service;

import com.saraiva.biblioteca.dto.BookReadRequest;
import com.saraiva.biblioteca.dto.BookRequest;
import com.saraiva.biblioteca.dto.BookResponse;
import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.entity.Book;
import com.saraiva.biblioteca.exception.ResourceNotFoundException;
import com.saraiva.biblioteca.mapper.BookMapper;
import com.saraiva.biblioteca.repository.AuthorRepository;
import com.saraiva.biblioteca.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.authorService = authorService;
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

    public void delete(Integer id){
        Book book = findById(id);
        bookRepository.delete(book);
    }

    public Book update(Integer id, BookRequest bookRequest){
        Book book = findById(id);
        Author author = authorService.findById(bookRequest.getAuthorId());
        book.setTitle(bookRequest.getTitle());
        book.setRead(bookRequest.getRead());
        book.setAuthor(author);
        book.setPublicationYear(bookRequest.getPublicationYear());
        return bookRepository.save(book);
    }

    public Book updateRead(Integer id, BookReadRequest bookReadRequest){
        Book book = findById(id);
        book.setRead(bookReadRequest.getRead());
        return bookRepository.save(book);
    }

    public List<BookResponse> findByAuthorId(Integer authorId){
        authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Author not found."));
        List<Book> list = bookRepository.findByAuthorId(authorId);
        List<BookResponse> responses = list.stream()
                .map(book -> BookMapper.toResponse(book))
                .toList();

        return responses;
    }

    public List<BookResponse> findByTitle(String title){
        List<Book> list = bookRepository.findByTitleContainingIgnoreCase(title);
        List<BookResponse> responses = list.stream()
                .map(book -> BookMapper.toResponse(book))
                .toList();

        return responses;
    }

    public List<BookResponse> findByRead(Boolean read){
        List<Book> list = bookRepository.findByIsRead(read);
        return list.stream()
                .map(book -> BookMapper.toResponse(book))
                .toList();
    }
}
