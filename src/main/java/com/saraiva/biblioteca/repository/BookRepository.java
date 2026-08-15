package com.saraiva.biblioteca.repository;

import com.saraiva.biblioteca.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
