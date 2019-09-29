package com.jproger.conferencetelegrambot.core.controllers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseActionController;
import com.jproger.conferencetelegrambot.common.actions.StatusRequestUserAction;
import com.jproger.conferencetelegrambot.core.operations.StatusRequestUserOperation;
import org.springframework.stereotype.Component;

@Component
public class StatusRequestUserActionController extends BaseActionController<StatusRequestUserAction> {
    private final StatusRequestUserOperation operation;

    public StatusRequestUserActionController(ActionBus actionBus, StatusRequestUserOperation operation) {
        super(StatusRequestUserAction.class, actionBus);

        this.operation = operation;
    }

    @Override
    protected void acceptTAction(StatusRequestUserAction action) {
        operation.execute(action.getChannel(), action.getChannelUserId());
    }
}
