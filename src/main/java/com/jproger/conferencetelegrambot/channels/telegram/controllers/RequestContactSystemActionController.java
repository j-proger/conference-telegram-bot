package com.jproger.conferencetelegrambot.channels.telegram.controllers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.channels.telegram.operations.RequestContactSystemOperation;
import com.jproger.conferencetelegrambot.common.actions.RequestContactSystemAction;
import com.jproger.conferencetelegrambot.common.operations.BaseActionController;
import org.springframework.stereotype.Component;

@Component
public class RequestContactSystemActionController extends BaseActionController<RequestContactSystemAction> {
    private final RequestContactSystemOperation operation;

    public RequestContactSystemActionController(ActionBus actionBus, RequestContactSystemOperation operation) {
        super(RequestContactSystemAction.class, actionBus);

        this.operation = operation;
    }

    @Override
    protected void acceptTAction(RequestContactSystemAction action) {
        operation.execute(action.getChannel(), action.getChannelUserId(), action.getMessage());
    }
}
