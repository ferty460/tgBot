package com.example.telegrambot.entity;

import lombok.Data;

@Data
public class BookEntity {
    private Long id;
    private String title;
    private AuthorEntity author;
    private PublisherEntity publisher;
    private int year;
    private String kind;

    @Override
    public String toString() {
        return "Название: " + title + '\n' +
                "Автор: " + author + '\n' +
                "Издательство: " + publisher + '\n' +
                "Год издания: " + year + '\n' +
                "Жанр: " + kind + '\n' + '\n';
    }
}
