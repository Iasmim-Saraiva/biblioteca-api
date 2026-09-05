package com.saraiva.biblioteca.repository;

import com.saraiva.biblioteca.dto.AuthorCountResponse;
import com.saraiva.biblioteca.dto.GenreCountResponse;
import com.saraiva.biblioteca.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    public List<Book> findByAuthorId(Integer id);

    @Query("""
            SELECT new com.saraiva.biblioteca.dto.GenreCountResponse(
                b.genre,
                COUNT(b)
                )
                FROM Book b
                GROUP BY b.genre
           """)
    public List<GenreCountResponse> countBooksByGenre();

    @Query("""
            SELECT new com.saraiva.biblioteca.dto.AuthorCountResponse(
                b.author.name,
                COUNT(b)
                )
                FROM Book b
                GROUP BY b.author.name
            """)
    public List<AuthorCountResponse> countBooksByAuthor();
}
