package com.example.telegrambot.response;

import com.example.telegrambot.entity.PublisherEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublisherListResponse extends BaseResponse{
    private Iterable<PublisherEntity> data;
    public PublisherListResponse(Iterable<PublisherEntity> data) {
        super(true, "Издательства");
        this.data = data;
    }
}
