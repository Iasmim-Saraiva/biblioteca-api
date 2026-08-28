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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.saraiva.biblioteca.specification.BookSpecification;
import org.springframework.data.jpa.domain.Specification;

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

    @Transactional(readOnly = true)
    public List<BookResponse> search(
            String title,
            String author,
            Boolean read,
            String genre,
            Integer minYear,
            Integer maxYear) {

        Specification<Book> spec = Specification.unrestricted();
        if (title != null) {
            spec = spec.and(BookSpecification.titleContains(title));
        }
        if (author != null) {
            spec = spec.and(BookSpecification.authorNameContains(author));
        }
        if (read != null) {
            spec = spec.and(BookSpecification.hasRead(read));
        }

        if (genre != null) {
            spec = spec.and(BookSpecification.hasGenre(genre));
        }
        if (minYear != null && maxYear != null) {
            spec = spec.and(
                    BookSpecification.publicationYearBetween(minYear, maxYear)
            );
        }
        List<Book> books = bookRepository.findAll(spec);

        return books.stream()
                .map(BookMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book findById(Integer id){
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    @Transactional
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
        book.setGenre(bookRequest.getGenre());

        return bookRepository.save(book);
    }

    @Transactional
    public void delete(Integer id){
        Book book = findById(id);
        bookRepository.delete(book);
    }

    @Transactional
    public Book update(Integer id, BookRequest bookRequest){
        Book book = findById(id);
        Author author = authorService.findById(bookRequest.getAuthorId());
        book.setTitle(bookRequest.getTitle());
        book.setRead(bookRequest.getRead());
        book.setAuthor(author);
        book.setPublicationYear(bookRequest.getPublicationYear());
        return book;
    }

    @Transactional
    public Book updateRead(Integer id, BookReadRequest bookReadRequest){
        Book book = findById(id);
        book.setRead(bookReadRequest.getRead());
        return book;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findByAuthorId(Integer authorId){
        authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Author not found."));
        List<Book> list = bookRepository.findByAuthorId(authorId);
        List<BookResponse> responses = list.stream()
                .map(book -> BookMapper.toResponse(book))
                .toList();

        return responses;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findByTitle(String title){
        List<Book> list = bookRepository.findByTitleContainingIgnoreCase(title);
        List<BookResponse> responses = list.stream()
                .map(book -> BookMapper.toResponse(book))
                .toList();

        return responses;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findByRead(Boolean read){
        List<Book> list = bookRepository.findByIsRead(read);
        return list.stream()
                .map(book -> BookMapper.toResponse(book))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findByAuthorName(String name){
        List<Book> list = bookRepository.findByAuthorNameContainingIgnoreCase(name);
        List<BookResponse> responses = list.stream()
                .map(book -> BookMapper.toResponse(book))
                .toList();
        return responses;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findByGenre(String genre){
        List<Book> list = bookRepository.findByGenreIgnoreCase(genre);
        List<BookResponse> responses = list.stream()
                .map(book -> BookMapper.toResponse(book))
                .toList();
        return responses;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findByPublicationYearBetween(Integer minYear, Integer maxYear){
        List<Book> list = bookRepository.findByPublicationYearBetween(minYear, maxYear);
        List<BookResponse> responses = list.stream()
                .map(book -> BookMapper.toResponse(book))
                .toList();
        return responses;
    }
}
