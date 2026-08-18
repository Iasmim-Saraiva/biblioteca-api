package com.saraiva.biblioteca.dto;

import jakarta.validation.constraints.NotNull;

public class BookReadRequest {

    @NotNull(message = "Read status is required")
    private Boolean read;

    public BookReadRequest(){
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
}
