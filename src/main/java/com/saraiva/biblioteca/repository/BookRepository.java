package com.saraiva.biblioteca.repository;

import com.saraiva.biblioteca.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    public List<Book> findByAuthorId(Integer id);

    public List<Book> findByTitleContainingIgnoreCase(String title);

    public List<Book> findByIsRead(Boolean read);

    public List<Book> findByAuthorNameContainingIgnoreCase(String name);

    public List<Book> findByGenreIgnoreCase(String genre);

    public List<Book> findByPublicationYearBetween(Integer minYear, Integer maxYear);
}
