package com.example.telegrambot.response;

import com.example.telegrambot.entity.BookEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BookResponse extends BaseResponse {
    private BookEntity data;
    public BookResponse(boolean success, String message, BookEntity data){
        super(success,message);
        this.data = data;
    }
    public BookResponse(BookEntity data) {
        super(true, "Book data");
    }
}
