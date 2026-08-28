package com.saraiva.biblioteca.controller;

import com.saraiva.biblioteca.dto.BookRequest;
import com.saraiva.biblioteca.dto.BookResponse;
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
    public void shouldReturnOkWhenGettingAllBooks() throws Exception {
        BookResponse response = new BookResponse();
        response.setId(1);
        response.setTitle("Dom Casmurro");
        response.setAuthorId(1);
        response.setAuthorName("Machado de Assis");

        List<BookResponse> responses = List.of(response);

        when(bookService.search(
                null,
                null,
                null,
                null,
                null,
                null
        )).thenReturn(responses);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Dom Casmurro"))
                .andExpect(jsonPath("$[0].authorId").value(1))
                .andExpect(jsonPath("$[0].authorName").value("Machado de Assis"));

        verify(bookService).search(
                null,
                null,
                null,
                null,
                null,
                null
        );
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
        request.setGenre("Romance");

        Author author = new Author();
        author.setId(1);
        author.setName("Machado de Assis");

        Book savedBook = new Book();
        savedBook.setTitle("Memórias Postumas de Brás Cubas");
        savedBook.setPublicationYear(1881);
        savedBook.setRead(true);
        savedBook.setAuthor(author);
        savedBook.setGenre("Romance");

        when(bookService.save(any(BookRequest.class))).thenReturn(savedBook);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Memórias Postumas de Brás Cubas"))
                .andExpect(jsonPath("$.authorId").value(1))
                .andExpect(jsonPath("$.authorName").value("Machado de Assis"))
                .andExpect(jsonPath("$.genre").value("Romance"));
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

    @Test
    public void shouldReturnBooksWhenSearchingByAuthorName() throws Exception{
        BookResponse response = new BookResponse();
        response.setId(1);
        response.setTitle("Dom Casmurro");
        response.setPublicationYear(1899);
        response.setRead(true);
        response.setAuthorId(7);
        response.setAuthorName("Machado de Assis");

        List<BookResponse> responses = List.of(response);

        when(bookService.search(
                null,
                "machado",
                null,
                null,
                null,
                null
        )).thenReturn(responses);

        mockMvc.perform(get("/books")
                .param("author", "machado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Dom Casmurro"))
                .andExpect(jsonPath("$[0].authorName").value("Machado de Assis"));

        verify(bookService).search(
                null,
                "machado",
                null,
                null,
                null,
                null
        );
    }

    @Test
    public void shouldReturnBooksWhenSearchingByGenre() throws Exception{
        BookResponse response = new BookResponse();
        response.setId(1);
        response.setTitle("Guerra");
        response.setRead(true);
        response.setPublicationYear(1989);
        response.setAuthorName("Machado");
        response.setAuthorId(7);
        response.setGenre("Fantasia");

        List<BookResponse> responses = List.of(response);

        when(bookService.search(
                null,
                null,
                null,
                "Fantasia",
                null,
                null
        )).thenReturn(responses);

        mockMvc.perform(get("/books")
                .param("genre", "Fantasia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].genre").value("Fantasia"));

        verify(bookService).search(
                null,
                null,
                null,
                "Fantasia",
                null,
                null
        );
    }

    @Test
    public void shouldReturnBooksWhenSearchingByPublicationYearBetween() throws Exception{
        BookResponse response = new BookResponse();
        response.setId(1);
        response.setTitle("Guerra");
        response.setRead(true);
        response.setPublicationYear(2019);
        response.setAuthorName("Machado");
        response.setAuthorId(7);
        response.setGenre("Fantasia");

        List<BookResponse> responses = List.of(response);

        when(bookService.search(
                null,
                null,
                null,
                null,
                2010,
                2020
        )).thenReturn(responses);

        mockMvc.perform(get("/books")
                .param("minYear", "2010")
                .param("maxYear", "2020"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].publicationYear").value(2019));

        verify(bookService).search(
                null,
                null,
                null,
                null,
                2010,
                2020
        );
    }
}
