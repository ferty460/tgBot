package com.example.telegrambot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class PublisherEntity {
    private Long id;
    private String publisher;
    private String city;
    private List<BookEntity> book;

    @Override
    public String toString() {
        return publisher;
    }

}
