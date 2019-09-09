package com.jproger.conferencetelegrambot.common.operations;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.ActionConsumer;
import com.jproger.conferencetelegrambot.core.operations.dto.Operation;
import com.jproger.conferencetelegrambot.core.operations.dto.Operation.ChannelType;
import com.jproger.conferencetelegrambot.core.operations.dto.SendTextMessageSystemOperation;
import com.jproger.conferencetelegrambot.common.operations.exceptions.UserActionException;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
public abstract class BaseOperationExecutor<T extends Operation> implements ActionConsumer {
    private static final String COMMON_ERROR_TEXT = "Извините, неизвестная мне ошибка...";

    private final Class<T> tClass;
    protected final ActionBus actionBus;

    @PostConstruct
    public void registerInBus() {
        actionBus.registerConsumer(this);
    }

    @Override
    public Class<? extends Operation> getActionClass() {
        return tClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void accept(Operation operation) {
        try {
            acceptTAction((T) operation);
        } catch (UserActionException ex) {
            sendTextMessageToUser(operation.getChannel(), operation.getChannelUserId(), ex.getMessage());
        } catch (Throwable ex) {
            sendTextMessageToUser(operation.getChannel(), operation.getChannelUserId(), COMMON_ERROR_TEXT);
        }
    }

    protected abstract void acceptTAction(T action);

    protected void sendTextMessageToUser(ChannelType channel, String channelUserId, String text) {
        SendTextMessageSystemOperation message = new SendTextMessageSystemOperation(channel, channelUserId, text);

        actionBus.sendAction(message);
    }
}
