package com.saraiva.biblioteca.dto;

public class AuthorResponse {

    private Integer id;
    private String name;

    public AuthorResponse(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
