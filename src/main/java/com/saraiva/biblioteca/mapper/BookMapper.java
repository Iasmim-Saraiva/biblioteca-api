package com.saraiva.biblioteca.mapper;

import com.saraiva.biblioteca.dto.BookResponse;
import com.saraiva.biblioteca.entity.Book;

public class BookMapper {
    public static BookResponse toResponse(Book book){

        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(book.getId());
        bookResponse.setTitle(book.getTitle());
        bookResponse.setPublicationYear(book.getPublicationYear());
        bookResponse.setRead(book.getRead());
        bookResponse.setAuthorId(book.getAuthor().getId());
        bookResponse.setAuthorName(book.getAuthor().getName());
        bookResponse.setGenre(book.getGenre());

        return bookResponse;
    }
}
