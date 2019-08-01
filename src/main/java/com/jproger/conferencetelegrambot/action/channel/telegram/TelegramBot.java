package com.jproger.conferencetelegrambot.action.channel.telegram;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.action.bus.dto.StartUserAction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final ActionBus actionBus;
    private final TelegramBotProperties telegramBotProperties;

    public TelegramBot(ActionBus actionBus,
                       TelegramBotProperties telegramBotProperties,
                       DefaultBotOptions options) {
        super(options);

        this.actionBus = actionBus;
        this.telegramBotProperties = telegramBotProperties;
    }

    @Override
    public String getBotUsername() {
        return telegramBotProperties.getName();
    }

    @Override
    public String getBotToken() {
        return telegramBotProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message.hasText() && isStartCommand(message.getText())) {
            actionBus.sendAction(createStartUserAction(message));
        }
    }

    private boolean isStartCommand(String text) {
        return text.toLowerCase().startsWith("/start");
    }

    private StartUserAction createStartUserAction(Message message) {
        String userId = message.getFrom().getId().toString();
        String topic = Arrays.stream(message.getText().split(" "))
                .skip(1)                            // убираем команду /start из потока
                .filter(StringUtils::isNoneBlank)   // фильтруем от пустых строк
                .findFirst().orElse(null);

        return new StartUserAction(ChannelType.TELEGRAM, userId, topic);
    }
}
