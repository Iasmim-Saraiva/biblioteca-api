package com.saraiva.biblioteca.controller;

import com.saraiva.biblioteca.dto.*;
import com.saraiva.biblioteca.entity.Book;
import com.saraiva.biblioteca.mapper.BookMapper;
import com.saraiva.biblioteca.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Books",
        description = "Operations for managing and searching books"
)
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @Operation(
            summary = "Search books",
            description = "Lists books using optional filters that can be combined, with pagination and sorting."
    )
    @GetMapping
    public Page<BookResponse> findAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean read,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            Pageable pageable){

        return bookService.search(title, author, read, genre, minYear, maxYear, pageable);
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

    @Operation(
            summary = "Update reading status",
            description = "Updates only the reading status of a book."
    )
    @PatchMapping("/{id}/read")
    public ResponseEntity<BookResponse> updateRead(@PathVariable Integer id, @Valid @RequestBody BookReadRequest bookReadRequest){
        Book book = bookService.updateRead(id, bookReadRequest);
        BookResponse response = BookMapper.toResponse(book);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(
            summary = "Count books",
            description = "Returns the total number of books in the library."
    )
    @GetMapping("/stats/count")
    public long countAllBooks(){
        return bookService.countAllBooks();
    }

    @Operation(
            summary = "Count books by genre",
            description = "Returns the number of books grouped by genre."
    )
    @GetMapping("/stats/genres")
    public List<GenreCountResponse> countBooksByGenre(){
        return bookService.countBooksByGenre();
    }

    @Operation(
            summary = "Count books by author",
            description = "Returns the number of books grouped by author."
    )
    @GetMapping("/stats/authors")
    public List<AuthorCountResponse> countBooksByAuthor(){
        return bookService.countBooksByAuthor();
    }
}
