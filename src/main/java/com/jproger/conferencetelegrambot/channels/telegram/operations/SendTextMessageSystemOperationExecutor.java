package com.jproger.conferencetelegrambot.channels.telegram.operations;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseOperationExecutor;
import com.jproger.conferencetelegrambot.core.operations.dto.SendTextMessageSystemOperation;
import com.jproger.conferencetelegrambot.channels.telegram.TelegramBot;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
public class SendTextMessageSystemOperationExecutor extends BaseOperationExecutor<SendTextMessageSystemOperation> {
    private final TelegramBot telegramBot;

    public SendTextMessageSystemOperationExecutor(ActionBus actionBus, TelegramBot telegramBot) {
        super(SendTextMessageSystemOperation.class, actionBus);

        this.telegramBot = telegramBot;
    }

    @Override
    public void acceptTAction(SendTextMessageSystemOperation action) {
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
