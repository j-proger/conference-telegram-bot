package com.jproger.conferencetelegrambot.action.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.dto.RequestContactSystemAction;
import com.jproger.conferencetelegrambot.channels.telegram.TelegramBot;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RequestContactSystemActionConsumer extends BaseActionConsumer<RequestContactSystemAction> {
    private final TelegramBot telegramBot;

    public RequestContactSystemActionConsumer(ActionBus actionBus, TelegramBot telegramBot) {
        super(RequestContactSystemAction.class, actionBus);

        this.telegramBot = telegramBot;
    }

    @Override
    public void acceptTAction(RequestContactSystemAction action) {
        switch (action.getChannel()) {
            case TELEGRAM:
                sendRequestContactMessageToTelegramChannel(action.getChannelUserId(), action.getMessage());
                break;
        }
    }

    @SneakyThrows
    private void sendRequestContactMessageToTelegramChannel(String userId, String text) {
        SendMessage message = new SendMessage();

        message.setReplyMarkup(getKeyboardMarkupForRequestContact());
        message.setChatId(userId);
        message.setText(text);

        telegramBot.execute(message);
    }

    private ReplyKeyboardMarkup getKeyboardMarkupForRequestContact() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton("Зарегистрироваться");

        button.setRequestContact(Boolean.TRUE);

        row.add(button);
        keyboard.add(row);
        markup.setKeyboard(keyboard);

        return markup;
    }
}
