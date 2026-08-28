package com.saraiva.biblioteca.specification;

import com.saraiva.biblioteca.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> hasGenre(String genre){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("genre")),
                        genre.toLowerCase()
                ));
    }

    public static Specification<Book> titleContains(String title){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"
                ));
    }

    public static Specification<Book> authorNameContains(String author){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("author").get("name")
                        ),
                        "%" + author.toLowerCase() + "%"
                ));
    }

    public static Specification<Book> hasRead(Boolean read){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isRead"), read));
    }

    public static Specification<Book> publicationYearBetween(Integer minYear, Integer maxYear){
        return  ((root, query, criteriaBuilder) ->
                criteriaBuilder.between(
                        root.get("publicationYear"),
                        minYear,
                        maxYear
                ));
    }
}
