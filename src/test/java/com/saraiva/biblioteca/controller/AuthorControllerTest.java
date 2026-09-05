package com.saraiva.biblioteca.controller;

import com.saraiva.biblioteca.dto.AuthorRequest;
import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.service.AuthorService;
import com.saraiva.biblioteca.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private BookService bookService;

    @Test
    public void shouldUpdateAuthor() throws Exception {

        AuthorRequest request = new AuthorRequest();
        request.setName("Machado de Assis");
        request.setNationality("Brasileiro");

        Author updatedAuthor = new Author();
        updatedAuthor.setId(1);
        updatedAuthor.setName("Machado de Assis");
        updatedAuthor.setNationality("Brasileiro");

        when(authorService.update(
                eq(1),
                any(AuthorRequest.class)
        )).thenReturn(updatedAuthor);

        mockMvc.perform(put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name")
                        .value("Machado de Assis"))
                .andExpect(jsonPath("$.nationality")
                        .value("Brasileiro"));

        ArgumentCaptor<AuthorRequest> requestCaptor = ArgumentCaptor.forClass(AuthorRequest.class);

        verify(authorService).update(
                eq(1),
                requestCaptor.capture()
        );

        AuthorRequest capturedRequest = requestCaptor.getValue();

        assertEquals(
                "Brasileiro",
                capturedRequest.getNationality()
        );
    }

    @Test
    public void shouldCreateAuthorWithoutNationality() throws Exception {
        AuthorRequest request = new AuthorRequest();
        request.setName("Clarice Lispector");

        Author savedAuthor = new Author();
        savedAuthor.setId(1);
        savedAuthor.setName("Clarice Lispector");

        when(authorService.save(any(AuthorRequest.class)))
                .thenReturn(savedAuthor);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(authorService).save(any(AuthorRequest.class));
    }
}
