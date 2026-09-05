package com.saraiva.biblioteca.dto;

public class AuthorCountResponse {

    private String authorName;
    private Long count;

    public AuthorCountResponse(String authorName, Long count) {
        this.authorName = authorName;
        this.count = count;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
