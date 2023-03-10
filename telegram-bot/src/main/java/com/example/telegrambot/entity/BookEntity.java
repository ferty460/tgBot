package com.example.telegrambot.entity;

import lombok.Data;

@Data
public class BookEntity {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private String kind;
}
