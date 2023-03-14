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
    public static ArrayList<BookEntity> booksData = new ArrayList<>();

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            String memberName = update.getMessage().getFrom().getFirstName();
            String[] param = messageText.split(" ");

            switch (param[0]) {
                case "/start" -> startBot(chatId, memberName);
                case "/all" -> getAllBook(chatId);
                case "/help" -> sendHelpText(chatId, HELP_TEXT);
                case "/search" -> searchBook(chatId, param[1]);
                case "/delete" -> deleteBook(chatId, param[1]);
                default -> log.info("Unexpected message");
            }
        }
    }

    public void searchBook(long chatId, String title) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        ResponseEntity<BookListResponse> responseEntity = new RestTemplate().
                getForEntity("http://localhost:2825/api/v1/book?title=" + title, BookListResponse.class);
        message.setText(responseEntity.getBody().getData().toString().replaceAll("^\\[|\\]$", ""));
        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public void deleteBook(long chatId, String id) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        System.out.println(booksData.size());
        if (Integer.parseInt(id) <= booksData.size() - 1 || !booksData.isEmpty()) {
            booksData.remove(Integer.parseInt(id));
            message.setText("Книга удалена");
        } else {
            message.setText("Такой книги нет");
        }
        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
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
        message.setText("Салам, " + userName + "! Я тг бот, я ничего не умею.");
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
