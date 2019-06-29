package com.jproger.conferencetelegrambot.telegram;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class TelegramBot extends TelegramLongPollingBot {
    private final String name;
    private final String token;
    private final Map<Integer, UserWorkflow> usersWorkflow = new HashMap<>();

    public TelegramBot(String name, String token, DefaultBotOptions options) {
        super(options);

        this.name = name;
        this.token = token;
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String messageText = message.getText();

        if (Objects.nonNull(messageText) && messageText.toLowerCase().startsWith("/start")) {
            initUserWorkflow(update);
            requestContact(update);
        } else if(message.hasContact()) {
            updateWorkflowToQuestion(update);
        }
    }

    private void initUserWorkflow(Update update) {
        Long chatId = update.getMessage().getChatId();
        User user = update.getMessage().getFrom();

        UserWorkflow userWorkflow = UserWorkflow.builder()
                .id(user.getId())
                .chatId(chatId)
                .state(UserWorkflow.State.SHARE_CONTACT)
                .build();

        usersWorkflow.put(user.getId(), userWorkflow);
    }

    private void requestContact(Update update) {
        Long chatId = update.getMessage().getChatId();
        SendMessage message = new SendMessage();

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton("Share contact");

        button.setRequestContact(Boolean.TRUE);

        row.add(button);
        keyboard.add(row);
        markup.setKeyboard(keyboard);

        message.setReplyMarkup(markup);
        message.setChatId(chatId);
        message.setText("Get me your phone number please");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Request contact exception");
            e.printStackTrace();
        }
    }

    private void updateWorkflowToQuestion(Update update) {
        Long chatId = update.getMessage().getChatId();
        Integer userId = update.getMessage().getFrom().getId();
        UserWorkflow userWorkflow = usersWorkflow.get(userId);

        Objects.requireNonNull(userWorkflow, "User workflow not found");

        userWorkflow.setState(UserWorkflow.State.ASK_QUESTION);
        sendMessage(chatId, "Congratulations, now you can ask questions! Each your message will be make a question.");
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();

        message.setText(text);
        message.setChatId(chatId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Message couldn't send");
            e.printStackTrace();
        }
    }
}
