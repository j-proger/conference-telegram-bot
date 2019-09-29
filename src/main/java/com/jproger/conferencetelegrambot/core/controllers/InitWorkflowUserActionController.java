package com.jproger.conferencetelegrambot.core.controllers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseActionController;
import com.jproger.conferencetelegrambot.common.actions.InitWorkflowUserAction;
import com.jproger.conferencetelegrambot.core.operations.InitWorkflowUserOperation;
import org.springframework.stereotype.Component;

@Component
public class InitWorkflowUserActionController extends BaseActionController<InitWorkflowUserAction> {
    private final InitWorkflowUserOperation operation;

    public InitWorkflowUserActionController(ActionBus actionBus, InitWorkflowUserOperation operation) {
        super(InitWorkflowUserAction.class, actionBus);

        this.operation = operation;
    }

    @Override
    protected void acceptTAction(InitWorkflowUserAction action) {
        operation.execute(action.getChannel(), action.getChannelUserId(), action.getTopicId());
    }
}
