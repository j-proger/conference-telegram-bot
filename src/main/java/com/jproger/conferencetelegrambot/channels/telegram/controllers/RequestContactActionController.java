package com.jproger.conferencetelegrambot.channels.telegram.controllers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.channels.telegram.operations.RequestContactOperation;
import com.jproger.conferencetelegrambot.common.actions.RequestContactSystemAction;
import com.jproger.conferencetelegrambot.common.operations.BaseActionController;
import org.springframework.stereotype.Component;

@Component
public class RequestContactActionController extends BaseActionController<RequestContactSystemAction> {
    private final RequestContactOperation operation;

    public RequestContactActionController(ActionBus actionBus, RequestContactOperation operation) {
        super(RequestContactSystemAction.class, actionBus);

        this.operation = operation;
    }

    @Override
    protected void acceptTAction(RequestContactSystemAction action) {
        operation.execute(action.getChannel(), action.getChannelUserId(), action.getMessage());
    }
}
