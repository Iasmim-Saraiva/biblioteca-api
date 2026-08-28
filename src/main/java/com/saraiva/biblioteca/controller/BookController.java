package com.saraiva.biblioteca.controller;

import com.saraiva.biblioteca.dto.BookReadRequest;
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
    public List<BookResponse> findAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean read,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear){

        return bookService.search(title, author, read, genre, minYear, maxYear);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        bookService.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Integer id, @Valid @RequestBody BookRequest bookRequest){
        Book updatedbook = bookService.update(id, bookRequest);
        BookResponse response = BookMapper.toResponse(updatedbook);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<BookResponse> updateRead(@PathVariable Integer id, @Valid @RequestBody BookReadRequest bookReadRequest){
        Book book = bookService.updateRead(id, bookReadRequest);
        BookResponse response = BookMapper.toResponse(book);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
