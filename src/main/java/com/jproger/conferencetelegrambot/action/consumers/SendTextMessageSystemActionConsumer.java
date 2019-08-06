package com.jproger.conferencetelegrambot.action.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.ActionConsumer;
import com.jproger.conferencetelegrambot.action.bus.dto.Action;
import com.jproger.conferencetelegrambot.action.bus.dto.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.channels.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendTextMessageSystemActionConsumer implements ActionConsumer {
    private final TelegramBot telegramBot;
    private final ActionBus actionBus;

    @PostConstruct
    public void registerInBus() {
        actionBus.registerConsumer(this);
    }

    @Override
    public Class<? extends Action> getActionClass() {
        return SendTextMessageSystemAction.class;
    }

    @Override
    public void accept(Action a) {
        SendTextMessageSystemAction action = (SendTextMessageSystemAction) a;

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
