package com.saraiva.biblioteca.controller;

import com.saraiva.biblioteca.dto.BookRequest;
import com.saraiva.biblioteca.dto.BookResponse;
import com.saraiva.biblioteca.entity.Book;
import com.saraiva.biblioteca.mapper.BookMapper;
import com.saraiva.biblioteca.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookResponse> findAll(){
        List<Book> books = bookService.findAll();
        List<BookResponse> responses = new ArrayList<>();

        for(Book book : books){
            responses.add(BookMapper.toResponse(book));
        }

        return responses;
    }

    @GetMapping("/{id}")
    public BookResponse findById(@PathVariable Integer id){
        Book book = bookService.findById(id);
        return BookMapper.toResponse(book);
    }

    @PostMapping
    public ResponseEntity<BookResponse> save(@Valid @RequestBody BookRequest bookRequest){
        Book savedbook = bookService.save(bookRequest);

        BookResponse response = BookMapper.toResponse(savedbook);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
