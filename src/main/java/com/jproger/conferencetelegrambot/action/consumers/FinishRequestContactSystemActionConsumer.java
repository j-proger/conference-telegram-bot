package com.jproger.conferencetelegrambot.action.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.ActionConsumer;
import com.jproger.conferencetelegrambot.action.bus.dto.Action;
import com.jproger.conferencetelegrambot.action.bus.dto.FinishRequestContactSystemAction;
import com.jproger.conferencetelegrambot.channels.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinishRequestContactSystemActionConsumer implements ActionConsumer {
    private final ActionBus actionBus;
    private final TelegramBot telegramBot;

    @PostConstruct
    public void registerInBus() {
        actionBus.registerConsumer(this);
    }

    @Override
    public Class<? extends Action> getActionClass() {
        return FinishRequestContactSystemAction.class;
    }

    @Override
    public void accept(Action a) {
        FinishRequestContactSystemAction action = (FinishRequestContactSystemAction) a;

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
