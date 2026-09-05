package com.saraiva.biblioteca.service;

import com.saraiva.biblioteca.dto.AuthorRequest;
import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.entity.Book;
import com.saraiva.biblioteca.exception.ResourceConflictException;
import com.saraiva.biblioteca.exception.ResourceNotFoundException;
import com.saraiva.biblioteca.repository.AuthorRepository;
import com.saraiva.biblioteca.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    public void shouldUpdateAuthor() {

        Author author = new Author();
        author.setId(1);
        author.setName("Nome errado");
        author.setNationality("Nacionalidade errada");

        AuthorRequest request = new AuthorRequest();
        request.setName("Machado de Assis");
        request.setNationality("Brasileiro");

        when(authorRepository.findById(1))
                .thenReturn(Optional.of(author));

        Author result = authorService.update(1, request);

        assertEquals("Machado de Assis", result.getName());
        assertEquals("Brasileiro", result.getNationality());

        verify(authorRepository).findById(1);
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNonExistingAuthor() {

        AuthorRequest request = new AuthorRequest();
        request.setName("Machado de Assis");

        when(authorRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.update(999, request)
        );

        verify(authorRepository).findById(999);
    }

    @Test
    public void shouldRemoveNationalityWhenUpdatingWithNullNationality() {
        Author author = new Author();
        author.setId(1);
        author.setName("Machado de Assis");
        author.setNationality("Brasileiro");

        AuthorRequest request = new AuthorRequest();
        request.setName("Machado de Assis");

        when(authorRepository.findById(1))
                .thenReturn(Optional.of(author));

        Author result = authorService.update(1, request);

        assertNull(result.getNationality());
    }

    @Test
    public void shouldSaveAuthorWithNationality() {
        AuthorRequest request = new AuthorRequest();
        request.setName("Clarice Lispector");
        request.setNationality("Brasileira");

        when(authorRepository.save(any(Author.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        authorService.save(request);

        ArgumentCaptor<Author> authorCaptor =
                ArgumentCaptor.forClass(Author.class);

        verify(authorRepository).save(authorCaptor.capture());

        Author savedAuthor = authorCaptor.getValue();

        assertEquals("Clarice Lispector", savedAuthor.getName());
        assertEquals("Brasileira", savedAuthor.getNationality());
    }
}
