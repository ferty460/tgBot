package com.example.telegrambot.response;

import com.example.telegrambot.entity.AuthorEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthorListResponse extends BaseResponse {
    private Iterable<AuthorEntity> data;
    public AuthorListResponse(Iterable<AuthorEntity> data) {
        super(true, "Authors");
        this.data = data;
    }
}
