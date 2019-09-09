package com.jproger.conferencetelegrambot.channels.telegram.operations;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseOperationExecutor;
import com.jproger.conferencetelegrambot.core.operations.dto.FinishRequestContactSystemOperation;
import com.jproger.conferencetelegrambot.channels.telegram.TelegramBot;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

@Slf4j
@Component
public class FinishRequestContactSystemOperationExecutor extends BaseOperationExecutor<FinishRequestContactSystemOperation> {
    private final TelegramBot telegramBot;

    public FinishRequestContactSystemOperationExecutor(ActionBus actionBus, TelegramBot telegramBot) {
        super(FinishRequestContactSystemOperation.class, actionBus);

        this.telegramBot = telegramBot;
    }

    @Override
    public void acceptTAction(FinishRequestContactSystemOperation action) {
        switch (action.getChannel()) {
            case TELEGRAM:
                sendThanksgivingMessageToTelegramChannel(action.getChannelUserId(), action.getMessage());
                break;
        }
    }

    @SneakyThrows
    private void sendThanksgivingMessageToTelegramChannel(String userId, String text) {
        SendMessage message = new SendMessage();

        message.setReplyMarkup(new ReplyKeyboardRemove());
        message.setChatId(userId);
        message.setText(text);

        telegramBot.execute(message);
    }
}
