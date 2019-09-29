package com.jproger.conferencetelegrambot.core.controllers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseActionController;
import com.jproger.conferencetelegrambot.common.actions.DefaultUserAction;
import com.jproger.conferencetelegrambot.core.operations.DefaultUserOperation;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserActionController extends BaseActionController<DefaultUserAction> {
    private final DefaultUserOperation defaultUserOperation;

    public DefaultUserActionController(ActionBus actionBus,
                                       DefaultUserOperation defaultUserOperation) {
        super(DefaultUserAction.class, actionBus);

        this.defaultUserOperation = defaultUserOperation;
    }

    @Override
    protected void acceptTAction(DefaultUserAction action) {
        defaultUserOperation.execute(action.getChannel(), action.getChannelUserId(), action.getText());
    }
}
