package com.saraiva.biblioteca.repository;

import com.saraiva.biblioteca.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    public List<Book> findByAuthorId(Integer id);

    public List<Book> findByTitleContainingIgnoreCase(String title);

    public List<Book> findByIsRead(Boolean read);

    Book id(Integer id);
}
