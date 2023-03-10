package com.example.telegrambot.response;

import com.example.telegrambot.entity.PublisherEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublisherResponse extends BaseResponse {
    private PublisherEntity publisher;
    public PublisherResponse(boolean success, String message, PublisherEntity publisher) {
        super(success, message);
        this.publisher = publisher;
    }
    public PublisherResponse(PublisherEntity publisher) {
        super(true, "Publishing data");
    }
}