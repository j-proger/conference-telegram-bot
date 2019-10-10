package com.jproger.conferencetelegrambot.channels.telegram.controllers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.channels.telegram.operations.FinishRequestContactOperation;
import com.jproger.conferencetelegrambot.common.actions.FinishRequestContactSystemAction;
import com.jproger.conferencetelegrambot.common.operations.BaseActionController;
import org.springframework.stereotype.Component;

@Component
public class FinishRequestContactActionController extends BaseActionController<FinishRequestContactSystemAction> {
    private final FinishRequestContactOperation operation;

    public FinishRequestContactActionController(ActionBus actionBus, FinishRequestContactOperation operation) {
        super(FinishRequestContactSystemAction.class, actionBus);

        this.operation = operation;
    }

    @Override
    protected void acceptTAction(FinishRequestContactSystemAction action) {
        operation.execute(action.getChannel(), action.getChannelUserId(), action.getMessage());
    }
}
