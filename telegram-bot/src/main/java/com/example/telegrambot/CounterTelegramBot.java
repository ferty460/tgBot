package com.example.telegrambot;

import com.example.telegrambot.components.BotCommands;
import com.example.telegrambot.components.Buttons;
import com.example.telegrambot.config.BotConfig;
import com.example.telegrambot.entity.BookEntity;
import com.example.telegrambot.response.BookListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Slf4j
@Component
public class CounterTelegramBot extends TelegramLongPollingBot implements BotCommands {
    final BotConfig config;

    public CounterTelegramBot(BotConfig config) {
        this.config = config;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        long chatId;
        String messageText;
        String memberName;
        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            messageText = update.getMessage().getText();
            memberName = update.getMessage().getFrom().getFirstName();
            String[] param = messageText.split(" ");
            switch (param[0]) {
                case "/start" -> startBot(chatId, memberName);
                case "/all" -> getAllBook(chatId);
                case "/help" -> sendHelpText(chatId, HELP_TEXT);
                case "/search" -> searchBook(chatId, param[1]);
                default -> log.info("Unexpected message");
            }
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            memberName = update.getCallbackQuery().getFrom().getFirstName();
            messageText = update.getCallbackQuery().getData();
            botAnswerUtils(messageText, chatId, memberName);
        }
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName) {
        switch (receivedMessage) {
            case "/start" -> startBot(chatId, userName);
            case "/help" -> sendHelpText(chatId, HELP_TEXT);
            case "/all" -> getAllBook(chatId);
        }
    }

    public void searchBook(long chatId, String title) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        ResponseEntity<BookListResponse> responseEntity = new RestTemplate().
                getForEntity("http://localhost:2825/api/v1/book?title=" + title, BookListResponse.class);
        message.setText(responseEntity.getBody().getData().toString().replaceAll("^\\[|\\]$", ""));
        if (message.getText().isEmpty()) {
            message.setText("Такой книги нет");
        }
        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException | ArrayIndexOutOfBoundsException e) {
            log.error(e.getMessage());
        }
    }

    private void getAllBook(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        ResponseEntity<BookListResponse> responseEntity = new RestTemplate().
                getForEntity("http://localhost:2825/api/v1/book/all", BookListResponse.class);
        System.out.println(responseEntity.getBody().getData());
        message.setText(responseEntity.getBody().getData().toString().replaceAll("^\\[|\\]$", ""));

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет, " + userName + "! Я тг бот, я ничего не умею.");
        message.setReplyMarkup(Buttons.inlineMarkup());

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }


    private void sendHelpText(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);


        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }
}
