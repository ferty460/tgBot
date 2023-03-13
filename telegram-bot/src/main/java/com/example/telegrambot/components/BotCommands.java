package com.example.telegrambot.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "запустить бота"),
            new BotCommand("/help", "помогите"),
            new BotCommand("/search", "поиск по названию"),
            new BotCommand("/all", "вывод книг")
    );

    String HELP_TEXT = "Доступные функции:\n\n" +
            "/start - запустить бота\n" +
            "/help - помогите\n" +
            "/search - поиск по названию\n" +
            "/all - вывод книг";
}
