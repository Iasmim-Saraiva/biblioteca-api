package com.saraiva.biblioteca.controller;

import com.saraiva.biblioteca.dto.BookRequest;
import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.entity.Book;
import com.saraiva.biblioteca.exception.ResourceNotFoundException;
import com.saraiva.biblioteca.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnOkWhenGettingAllBooks() throws Exception{
        Author author = new Author();
        author.setName("Machado de Assis");
        author.setId(1);

        Book book = new Book();
        book.setTitle("Dom Casmurro");
        book.setAuthor(author);

        List<Book> books = List.of(book);

        when(bookService.findAll()).thenReturn(books);
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Dom Casmurro"))
                .andExpect(jsonPath("$[0].authorId").value(1))
                .andExpect(jsonPath("$[0].authorName").value("Machado de Assis"));
    }

    @Test
    public void shouldReturnBookWhenIdExists() throws Exception{
        Author author = new Author();
        author.setName("Socorro Acioli");
        author.setId(2);

        Book book = new Book();
        book.setTitle("A Cabeça do Santo");
        book.setAuthor(author);

        when(bookService.findById(1)).thenReturn(book);
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("A Cabeça do Santo"))
                .andExpect(jsonPath("$.authorId").value(2))
                .andExpect(jsonPath("$.authorName").value("Socorro Acioli"));
    }

    @Test
    public void shouldReturnNotFoundWhenBookDoesNotExist() throws Exception{
        when(bookService.findById(999)).thenThrow(new ResourceNotFoundException("Book not found"));
        mockMvc.perform(get("/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    public void shouldCreateBookWhenRequestIsValid() throws Exception{
        BookRequest request = new BookRequest();
        request.setTitle("Memórias Póstumas de Brás Cubas");
        request.setPublicationYear(1881);
        request.setRead(true);
        request.setAuthorId(1);

        Author author = new Author();
        author.setId(1);
        author.setName("Machado de Assis");

        Book savedBook = new Book();
        savedBook.setTitle("Memórias Postumas de Brás Cubas");
        savedBook.setPublicationYear(1881);
        savedBook.setRead(true);
        savedBook.setAuthor(author);

        when(bookService.save(any(BookRequest.class))).thenReturn(savedBook);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Memórias Postumas de Brás Cubas"))
                .andExpect(jsonPath("$.authorId").value(1))
                .andExpect(jsonPath("$.authorName").value("Machado de Assis"));
    }

    @Test
    public void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception{
        BookRequest request = new BookRequest();
        request.setTitle("");
        request.setPublicationYear(1881);
        request.setRead(true);
        request.setAuthorId(1);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).save(any(BookRequest.class));
    }
}
