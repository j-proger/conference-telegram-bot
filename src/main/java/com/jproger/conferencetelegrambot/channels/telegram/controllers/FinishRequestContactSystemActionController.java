package com.jproger.conferencetelegrambot.channels.telegram.controllers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.channels.telegram.operations.FinishRequestContactSystemOperation;
import com.jproger.conferencetelegrambot.common.actions.FinishRequestContactSystemAction;
import com.jproger.conferencetelegrambot.common.operations.BaseActionController;
import org.springframework.stereotype.Component;

@Component
public class FinishRequestContactSystemActionController extends BaseActionController<FinishRequestContactSystemAction> {
    private final FinishRequestContactSystemOperation operation;

    public FinishRequestContactSystemActionController(ActionBus actionBus, FinishRequestContactSystemOperation operation) {
        super(FinishRequestContactSystemAction.class, actionBus);

        this.operation = operation;
    }

    @Override
    protected void acceptTAction(FinishRequestContactSystemAction action) {
        operation.execute(action.getChannel(), action.getChannelUserId(), action.getMessage());
    }
}
