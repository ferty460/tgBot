package com.example.telegrambot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class AuthorEntity {
    private Long id;
    private String name;
    private String lastname;
    private String surname;
    private List<BookEntity> book;

    @Override
    public String toString() {
        return name + ' ' + surname;
    }
}
