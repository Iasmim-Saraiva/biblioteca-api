package com.saraiva.biblioteca.controller;

import com.saraiva.biblioteca.dto.BookRequest;
import com.saraiva.biblioteca.dto.BookResponse;
import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.entity.Book;
import com.saraiva.biblioteca.exception.ResourceNotFoundException;
import com.saraiva.biblioteca.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        Page<BookResponse> page = new PageImpl<>(responses);

        when(bookService.search(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Dom Casmurro"))
                .andExpect(jsonPath("$.content[0].authorId").value(1))
                .andExpect(jsonPath("$.content[0].authorName").value("Machado de Assis"));

        verify(bookService).search(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)
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
        Page<BookResponse> page = new PageImpl<>(responses);

        when(bookService.search(
                isNull(),
                eq("machado"),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/books")
                .param("author", "machado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Dom Casmurro"))
                .andExpect(jsonPath("$.content[0].authorName").value("Machado de Assis"));

        verify(bookService).search(
                isNull(),
                eq("machado"),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)
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
        Page<BookResponse> page = new PageImpl<>(responses);

        when(bookService.search(
                isNull(),
                isNull(),
                isNull(),
                eq("Fantasia"),
                isNull(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/books")
                .param("genre", "Fantasia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].genre").value("Fantasia"));

        verify(bookService).search(
                isNull(),
                isNull(),
                isNull(),
                eq("Fantasia"),
                isNull(),
                isNull(),
                any(Pageable.class)
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
        Page<BookResponse> page = new PageImpl<>(responses);


        when(bookService.search(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(2010),
                eq(2020),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/books")
                .param("minYear", "2010")
                .param("maxYear", "2020"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].publicationYear").value(2019));

        verify(bookService).search(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(2010),
                eq(2020),
                any(Pageable.class)
        );
    }

    @Test
    public void shouldPassPaginationParametersToService() throws Exception{
        Page<BookResponse> page = new PageImpl<>(List.of());

        when(bookService.search(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/books")
                .param("page", "2")
                .param("size", "5"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(bookService).search(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                pageableCaptor.capture()
        );

        Pageable pageable = pageableCaptor.getValue();

        assertEquals(2, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
    }

    @Test
    public void shouldPassSortingParametersToService() throws Exception{
        Page<BookResponse> page = new PageImpl<>(List.of());

        when(bookService.search(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/books")
                .param("sort", "publicationYear,desc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(bookService).search(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                pageableCaptor.capture()
        );

        Pageable pageable = pageableCaptor.getValue();

        Sort.Order publicationYearOrder = pageable.getSort().getOrderFor("publicationYear");
        assertNotNull(publicationYearOrder);
        assertEquals(Sort.Direction.DESC, publicationYearOrder.getDirection());
    }
}
