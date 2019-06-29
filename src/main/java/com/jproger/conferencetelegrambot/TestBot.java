package com.jproger.conferencetelegrambot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TestBot extends TelegramLongPollingBot {
    public TestBot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();

        SendMessage reply = new SendMessage(chatId, "ReplyTo: " + message);

        try {
            this.execute(reply);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "ConferenceRequesterBot";
    }

    @Override
    public String getBotToken() {
        return "868656591:AAEN_gOc2GQa4zo5Gp4KWPq1Rt_XgZBuSgY";
    }
}
