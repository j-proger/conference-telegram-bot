package com.jproger.conferencetelegrambot.common.operations;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.ActionConsumer;
import com.jproger.conferencetelegrambot.common.actions.Action;
import com.jproger.conferencetelegrambot.common.actions.Action.ChannelType;
import com.jproger.conferencetelegrambot.common.actions.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.common.operations.exceptions.UserActionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseActionController<T extends Action> implements ActionConsumer {
    private static final String COMMON_ERROR_TEXT = "Извините, неизвестная мне ошибка...";

    private final Class<T> tClass;
    protected final ActionBus actionBus;

    @PostConstruct
    public void registerInBus() {
        actionBus.registerConsumer(this);
    }

    @Override
    public Class<? extends Action> getActionClass() {
        return tClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void accept(Action action) {
        try {
            acceptTAction((T) action);
        } catch (UserActionException ex) {
            log.error("USER ACTION EXCEPTION action: {}", action, ex);

            sendTextMessageToUser(action.getChannel(), action.getChannelUserId(), ex.getMessage());
        } catch (Throwable ex) {
            log.error("THROWABLE action: {}", action, ex);

            sendTextMessageToUser(action.getChannel(), action.getChannelUserId(), COMMON_ERROR_TEXT);
        }
    }

    protected abstract void acceptTAction(T action);

    protected void sendTextMessageToUser(ChannelType channel, String channelUserId, String text) {
        SendTextMessageSystemAction message = new SendTextMessageSystemAction(channel, channelUserId, text);

        actionBus.sendAction(message);
    }
}
