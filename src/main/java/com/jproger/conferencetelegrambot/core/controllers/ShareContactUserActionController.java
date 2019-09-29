package com.jproger.conferencetelegrambot.core.controllers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseActionController;
import com.jproger.conferencetelegrambot.common.actions.ShareContactUserAction;
import com.jproger.conferencetelegrambot.core.operations.ShareContactUserOperation;
import org.springframework.stereotype.Component;

@Component
public class ShareContactUserActionController extends BaseActionController<ShareContactUserAction> {
    private final ShareContactUserOperation operation;

    public ShareContactUserActionController(ActionBus actionBus, ShareContactUserOperation operation) {
        super(ShareContactUserAction.class, actionBus);

        this.operation = operation;
    }

    @Override
    protected void acceptTAction(ShareContactUserAction action) {
        operation.execute(
                action.getChannel(),
                action.getChannelUserId(),
                action.getPhoneNumber(),
                action.getFirstName(),
                action.getLastName()
        );
    }
}
