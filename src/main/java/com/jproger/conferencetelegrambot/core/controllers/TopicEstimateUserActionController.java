package com.jproger.conferencetelegrambot.core.controllers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.actions.TopicEstimateUserAction;
import com.jproger.conferencetelegrambot.common.operations.BaseActionController;
import com.jproger.conferencetelegrambot.core.operations.TopicEstimateUserOperation;
import org.springframework.stereotype.Component;

@Component
public class TopicEstimateUserActionController extends BaseActionController<TopicEstimateUserAction> {
    private final TopicEstimateUserOperation operation;

    public TopicEstimateUserActionController(ActionBus actionBus, TopicEstimateUserOperation operation) {
        super(TopicEstimateUserAction.class, actionBus);

        this.operation = operation;
    }

    @Override
    protected void acceptTAction(TopicEstimateUserAction action) {
        operation.execute(
                action.getChannel(),
                action.getChannelUserId(),
                action.getTopicId(),
                action.getRating()
        );
    }
}
