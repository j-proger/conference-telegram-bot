package com.jproger.conferencetelegrambot.common.operations;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.actions.Action;
import com.jproger.conferencetelegrambot.common.actions.SendTextMessageSystemAction;

public abstract class BaseOperation {
    protected final ActionBus actionBus;

    public BaseOperation(ActionBus actionBus) {
        this.actionBus = actionBus;
    }

    protected void sendTextMessageToUser(Action.ChannelType channel, String channelUserId, String text) {
        SendTextMessageSystemAction message = new SendTextMessageSystemAction(channel, channelUserId, text);

        actionBus.sendAction(message);
    }
}
