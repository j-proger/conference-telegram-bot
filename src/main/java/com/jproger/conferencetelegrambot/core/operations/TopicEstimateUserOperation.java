package com.jproger.conferencetelegrambot.core.operations;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.actions.Action.ChannelType;
import com.jproger.conferencetelegrambot.common.operations.BaseOperation;
import com.jproger.conferencetelegrambot.common.operations.exceptions.UserActionException;
import com.jproger.conferencetelegrambot.topics.TopicService;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import com.jproger.conferencetelegrambot.workflow.entities.Status;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@Component
public class TopicEstimateUserOperation extends BaseOperation {
    private final static List<Status> ALLOWED_STATUSES = Arrays.asList(
            Status.COLLECT_QUESTIONS,
            Status.COLLECT_FEEDBACK,
            Status.REGISTERED
    );

    private final TopicService topicService;
    private final UserStateService userStateService;

    public TopicEstimateUserOperation(ActionBus actionBus,
                                      TopicService topicService,
                                      UserStateService userStateService) {
        super(actionBus);

        this.topicService = topicService;
        this.userStateService = userStateService;
    }

    public void execute(@Nonnull ChannelType channel,
                        @Nonnull String channelUserId,
                        long topicId,
                        int rating) {
        UserStateDto state = userStateService.getUserStateByChannelAndChannelUserId(channel, channelUserId)
                .orElseThrow(() -> new UserActionException("Я вас еще не знаю..."));

        if (!ALLOWED_STATUSES.contains(state.getStatus())) {
            throw new UserActionException("Вы еще не зарегистрированы...");
        }

        createTopicEstimate(state, topicId, rating);

        sendTextMessageToUser(channel, channelUserId, "Спасибо за оценку доклада!");
    }

    private void createTopicEstimate(UserStateDto state, long topicId, int rating) {
        topicService.createRating(state.getInnerUserId(), topicId, rating)
                .orElseThrow(() -> new UserActionException("Не удалось оценить доклад. Возможно указан некорректный идентификатор доклада."));
    }
}
