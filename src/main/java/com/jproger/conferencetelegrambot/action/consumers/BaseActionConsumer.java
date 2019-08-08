package com.jproger.conferencetelegrambot.action.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.ActionConsumer;
import com.jproger.conferencetelegrambot.action.bus.dto.Action;
import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.action.bus.dto.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.action.consumers.exceptions.UserActionException;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
public abstract class BaseActionConsumer<T extends Action> implements ActionConsumer {
    private static final String COMMON_ERROR_TEXT = "Sorry, something went wrong...";

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
            sendTextMessageToUser(action.getChannel(), action.getChannelUserId(), ex.getMessage());
        } catch (Throwable ex) {
            sendTextMessageToUser(action.getChannel(), action.getChannelUserId(), COMMON_ERROR_TEXT);
        }
    }

    protected abstract void acceptTAction(T action);

    protected void sendTextMessageToUser(ChannelType channel, String channelUserId, String text) {
        SendTextMessageSystemAction message = new SendTextMessageSystemAction(channel, channelUserId, text);

        actionBus.sendAction(message);
    }
}
