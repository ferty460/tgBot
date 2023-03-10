package com.example.telegrambot.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BaseResponse { // хаптьфу
    protected boolean success;
    protected String message;
}

