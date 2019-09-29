package com.jproger.conferencetelegrambot.channels.telegram.controllers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.channels.telegram.operations.SendTextMessageSystemOperation;
import com.jproger.conferencetelegrambot.common.actions.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.common.operations.BaseActionController;
import org.springframework.stereotype.Component;

@Component
public class SendTextMessageSystemActionController extends BaseActionController<SendTextMessageSystemAction> {
    private final SendTextMessageSystemOperation operation;

    public SendTextMessageSystemActionController(ActionBus actionBus, SendTextMessageSystemOperation operation) {
        super(SendTextMessageSystemAction.class, actionBus);

        this.operation = operation;
    }

    @Override
    protected void acceptTAction(SendTextMessageSystemAction action) {
        operation.execute(action.getChannel(), action.getChannelUserId(), action.getMessage());
    }
}
