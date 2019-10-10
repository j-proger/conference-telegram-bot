package com.jproger.conferencetelegrambot.channels.telegram.controllers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.channels.telegram.operations.SendTextMessageOperation;
import com.jproger.conferencetelegrambot.common.actions.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.common.operations.BaseActionController;
import org.springframework.stereotype.Component;

@Component
public class SendTextMessageActionController extends BaseActionController<SendTextMessageSystemAction> {
    private final SendTextMessageOperation operation;

    public SendTextMessageActionController(ActionBus actionBus, SendTextMessageOperation operation) {
        super(SendTextMessageSystemAction.class, actionBus);

        this.operation = operation;
    }

    @Override
    protected void acceptTAction(SendTextMessageSystemAction action) {
        operation.execute(action.getChannel(), action.getChannelUserId(), action.getMessage());
    }
}
