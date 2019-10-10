package com.jproger.conferencetelegrambot.core.controllers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseActionController;
import com.jproger.conferencetelegrambot.common.actions.FinishTopicSystemAction;
import com.jproger.conferencetelegrambot.core.operations.FinishTopicOperation;
import org.springframework.stereotype.Component;

@Component
public class FinishTopicSystemActionController extends BaseActionController<FinishTopicSystemAction> {
    private final FinishTopicOperation operation;

    public FinishTopicSystemActionController(ActionBus actionBus, FinishTopicOperation operation) {
        super(FinishTopicSystemAction.class, actionBus);

        this.operation = operation;
    }

    @Override
    protected void acceptTAction(FinishTopicSystemAction action) {
        operation.execute(action.getTopicId());
    }
}
