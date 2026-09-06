package com.saraiva.biblioteca.repository;

import com.saraiva.biblioteca.dto.GenreCountResponse;
import com.saraiva.biblioteca.entity.Author;
import com.saraiva.biblioteca.entity.Book;
import com.saraiva.biblioteca.specification.BookSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void shouldUseTestDatabase() throws SQLException{
        try(Connection connection = dataSource.getConnection()){
            assertEquals(
                    "biblioteca-saraiva-test",
                    connection.getCatalog()
            );
        }
    }

    @Test
    public void shouldCountBooksByGenre(){
        Author author = new Author();
        author.setName("Autor teste");

        Author savedAuthor = authorRepository.save(author);

        Book b1 = new Book();
        b1.setTitle("Teste 1");
        b1.setGenre("Terror");
        b1.setAuthor(savedAuthor);
        b1.setRead(false);
        b1.setPublicationYear(2021);

        Book b2 = new Book();
        b2.setTitle("Teste 2");
        b2.setGenre("Fantasia");
        b2.setAuthor(savedAuthor);
        b2.setRead(false);
        b2.setPublicationYear(2020);

        Book b3 = new Book();
        b3.setTitle("Teste 3");
        b3.setGenre("Fantasia");
        b3.setAuthor(savedAuthor);
        b3.setRead(false);
        b3.setPublicationYear(2015);

        bookRepository.save(b1);
        bookRepository.save(b2);
        bookRepository.save(b3);

        List<GenreCountResponse> result = bookRepository.countBooksByGenre();

        assertEquals(2, result.size());

        GenreCountResponse fantasyResult = result.stream()
                .filter(item -> item.getGenre().equals("Fantasia"))
                .findFirst()
                .orElseThrow();

        GenreCountResponse horrorResult = result.stream()
                .filter(item -> item.getGenre().equals("Terror"))
                .findFirst()
                .orElseThrow();

        assertEquals(2L, fantasyResult.getCount());
        assertEquals(1L, horrorResult.getCount());
    }

    @Test
    public void shouldFilterBooksByGenreAndReadStatus() {
        Author author = new Author();
        author.setName("Autor teste");

        Author savedAuthor = authorRepository.save(author);

        Book b1 = new Book();
        b1.setTitle("Livro A");
        b1.setGenre("Terror");
        b1.setAuthor(savedAuthor);
        b1.setRead(false);
        b1.setPublicationYear(2021);

        Book b2 = new Book();
        b2.setTitle("Livro B");
        b2.setGenre("Fantasia");
        b2.setAuthor(savedAuthor);
        b2.setRead(true);
        b2.setPublicationYear(2020);

        Book b3 = new Book();
        b3.setTitle("Livro C");
        b3.setGenre("Fantasia");
        b3.setAuthor(savedAuthor);
        b3.setRead(false);
        b3.setPublicationYear(2015);

        bookRepository.save(b1);
        bookRepository.save(b2);
        bookRepository.save(b3);

        Specification<Book> spec = Specification.unrestricted();
        spec = spec.and(BookSpecification.hasGenre("Fantasia"));
        spec = spec.and(BookSpecification.hasRead(true));

        List<Book> result = bookRepository.findAll(spec);

        assertEquals(1, result.size());
        assertEquals("Livro B", result.get(0).getTitle());
    }
}
