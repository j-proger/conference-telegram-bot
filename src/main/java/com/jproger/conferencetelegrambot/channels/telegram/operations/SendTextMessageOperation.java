package com.jproger.conferencetelegrambot.channels.telegram.operations;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.channels.telegram.TelegramBot;
import com.jproger.conferencetelegrambot.common.operations.BaseOperation;
import com.jproger.conferencetelegrambot.common.actions.Action.ChannelType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.annotation.Nonnull;

@Slf4j
@Component
public class SendTextMessageOperation extends BaseOperation {
    private final TelegramBot telegramBot;

    public SendTextMessageOperation(ActionBus actionBus, TelegramBot telegramBot) {
        super(actionBus);

        this.telegramBot = telegramBot;
    }

    public void execute(@Nonnull ChannelType channel,
                        @Nonnull String channelUserId,
                        @Nonnull String message) {
        switch (channel) {
            case TELEGRAM:
                sendTextMessageToTelegramBot(channelUserId, message);
                break;
        }
    }

    @SneakyThrows
    private void sendTextMessageToTelegramBot(String recipientId, String text) {
        SendMessage message = new SendMessage(recipientId, text);

        telegramBot.execute(message);
    }
}
