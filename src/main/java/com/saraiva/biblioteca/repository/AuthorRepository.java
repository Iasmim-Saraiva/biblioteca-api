package com.saraiva.biblioteca.repository;

import com.saraiva.biblioteca.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
