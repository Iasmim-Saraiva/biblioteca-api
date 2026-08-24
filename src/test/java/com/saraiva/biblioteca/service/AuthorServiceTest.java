package com.saraiva.biblioteca.service;

import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.entity.Book;
import com.saraiva.biblioteca.exception.ResourceConflictException;
import com.saraiva.biblioteca.exception.ResourceNotFoundException;
import com.saraiva.biblioteca.repository.AuthorRepository;
import com.saraiva.biblioteca.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    private AuthorService authorService;

    @BeforeEach
    public void initializeAuthorService(){
        authorService = new AuthorService(authorRepository, bookRepository);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingAuthorWithBooks(){
        Author author = new Author();
        Book book = new Book();
        List<Book> books = List.of(book);
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(bookRepository.findByAuthorId(1)).thenReturn(books);

        assertThrows(ResourceConflictException.class, () -> authorService.delete(1));

        verify(authorRepository, never()).delete(any());
    }
}
