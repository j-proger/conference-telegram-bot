package com.jproger.conferencetelegrambot.action.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.dto.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.channels.telegram.TelegramBot;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
public class SendTextMessageSystemActionConsumer extends BaseActionConsumer<SendTextMessageSystemAction> {
    private final TelegramBot telegramBot;

    public SendTextMessageSystemActionConsumer(ActionBus actionBus, TelegramBot telegramBot) {
        super(SendTextMessageSystemAction.class, actionBus);

        this.telegramBot = telegramBot;
    }

    @Override
    public void acceptTAction(SendTextMessageSystemAction action) {
        switch (action.getChannel()) {
            case TELEGRAM:
                sendTextMessageToTelegramBot(action.getChannelUserId(), action.getMessage());
                break;
        }
    }

    @SneakyThrows
    private void sendTextMessageToTelegramBot(String recipientId, String text) {
        SendMessage message = new SendMessage(recipientId, text);

        telegramBot.execute(message);
    }
}
