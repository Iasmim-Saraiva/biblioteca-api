package com.saraiva.biblioteca.service;

import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.entity.Book;
import com.saraiva.biblioteca.exception.ResourceNotFoundException;
import com.saraiva.biblioteca.repository.AuthorRepository;
import com.saraiva.biblioteca.repository.BookRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorService authorService;

    private BookService bookService;

    @BeforeEach
    public void initializeBookService(){
        bookService = new BookService(bookRepository, authorRepository, authorService);
    }

    @Test
    public void shouldReturnBookWhenIdExists(){
        Author author = new Author();
        author.setName("Autor Teste Service");

        Book book = new Book();
        book.setTitle("Livro Teste Service");
        book.setRead(true);
        book.setPublicationYear(2026);
        book.setAuthor(author);

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        Book result = bookService.findById(1);

        assertEquals(book, result);

        verify(bookRepository).findById(1);
    }

    @Test
    public void shouldThrowExceptionWhenIdDoesNotExist(){
        when(bookRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.findById(999));
    }

    @Test
    public void shouldDeleteBookWhenIdExists(){
        Book book = new Book();
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        
        bookService.delete(1);

        verify(bookRepository).delete(book);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingNonExistingBook(){
        when(bookRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.delete(999));

        verify(bookRepository, never()).delete(any(Book.class));
    }
}
