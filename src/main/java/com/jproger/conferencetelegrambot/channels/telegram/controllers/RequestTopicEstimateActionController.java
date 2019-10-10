package com.jproger.conferencetelegrambot.channels.telegram.controllers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.channels.telegram.operations.RequestTopicEstimateOperation;
import com.jproger.conferencetelegrambot.common.actions.RequestTopicEstimateSystemAction;
import com.jproger.conferencetelegrambot.common.operations.BaseActionController;
import org.springframework.stereotype.Component;

@Component
public class RequestTopicEstimateActionController extends BaseActionController<RequestTopicEstimateSystemAction> {
    private final RequestTopicEstimateOperation operation;

    public RequestTopicEstimateActionController(ActionBus actionBus, RequestTopicEstimateOperation operation) {
        super(RequestTopicEstimateSystemAction.class, actionBus);

        this.operation = operation;
    }

    @Override
    protected void acceptTAction(RequestTopicEstimateSystemAction action) {
        switch (action.getChannel()) {
            case TELEGRAM:
                operation.execute(action.getChannelUserId(), action.getTopicId());
        }
    }
}
