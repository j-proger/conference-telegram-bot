package com.jproger.conferencetelegrambot.channels.telegram.operations;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.channels.telegram.TelegramBot;
import com.jproger.conferencetelegrambot.common.operations.BaseOperation;
import com.jproger.conferencetelegrambot.common.actions.Action.ChannelType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RequestContactSystemOperation extends BaseOperation {
    private final TelegramBot telegramBot;

    public RequestContactSystemOperation(ActionBus actionBus, TelegramBot telegramBot) {
        super(actionBus);

        this.telegramBot = telegramBot;
    }

    public void execute(@Nonnull ChannelType channel,
                           @Nonnull String channelUserId,
                           @Nonnull String message) {
        switch (channel) {
            case TELEGRAM:
                sendRequestContactMessageToTelegramChannel(channelUserId, message);
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
