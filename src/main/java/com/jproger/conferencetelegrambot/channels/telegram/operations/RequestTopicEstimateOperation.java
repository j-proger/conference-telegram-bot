package com.jproger.conferencetelegrambot.channels.telegram.operations;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.channels.telegram.TelegramBot;
import com.jproger.conferencetelegrambot.channels.telegram.TelegramCommandConstants;
import com.jproger.conferencetelegrambot.common.operations.BaseOperation;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;

@Component
public class RequestTopicEstimateOperation extends BaseOperation {
    private final TelegramBot telegramBot;

    public RequestTopicEstimateOperation(ActionBus actionBus, TelegramBot telegramBot) {
        super(actionBus);

        this.telegramBot = telegramBot;
    }

    @SneakyThrows
    public void execute(@Nonnull String channelUserId,
                        long topicId) {
        SendMessage message = new SendMessage();

        message.setChatId(channelUserId);
        message.setReplyMarkup(buildKeyboardMarkupForRatingTopic(topicId));
        message.setText("Выбранный вами доклад завершен. Оцените его :-)");

        telegramBot.execute(message);
    }

    private ReplyKeyboard buildKeyboardMarkupForRatingTopic(long topicId) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        keyboard.setKeyboard(Collections.singletonList(
                Arrays.asList(
                        buildKeyboardButton(topicId, 1),
                        buildKeyboardButton(topicId, 2),
                        buildKeyboardButton(topicId, 3),
                        buildKeyboardButton(topicId, 4),
                        buildKeyboardButton(topicId, 5)
                )
        ));

        return keyboard;
    }

    private InlineKeyboardButton buildKeyboardButton(long topicId, int rating) {
        InlineKeyboardButton button = new InlineKeyboardButton();

        button.setText(String.valueOf(rating));
        button.setCallbackData(buildCallbackRatingData(topicId, rating));

        return button;
    }

    @SneakyThrows
    private String buildCallbackRatingData(long topicId, int rating) {
        return TelegramCommandConstants.TOPIC_ESTIMATE_COMMAND + " " + topicId + " " + rating;
    }
}
