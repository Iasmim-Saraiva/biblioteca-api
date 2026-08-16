package com.saraiva.biblioteca.controller;

import com.saraiva.biblioteca.entity.Book;
import com.saraiva.biblioteca.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> findAll(){
        return bookService.findAll();

    }

    @GetMapping("/{id}")
    public Book findById(@PathVariable Integer id){
        return bookService.findById(id);
    }

    @PostMapping
    public Book save(@RequestBody Book book){
        return bookService.save(book);
    }
}
