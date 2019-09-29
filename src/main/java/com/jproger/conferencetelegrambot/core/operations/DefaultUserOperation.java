package com.jproger.conferencetelegrambot.core.operations;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.actions.Action;
import com.jproger.conferencetelegrambot.common.operations.BaseOperation;
import com.jproger.conferencetelegrambot.common.operations.exceptions.UserActionException;
import com.jproger.conferencetelegrambot.feedback.FeedbackService;
import com.jproger.conferencetelegrambot.topics.TopicService;
import com.jproger.conferencetelegrambot.users.UserService;
import com.jproger.conferencetelegrambot.users.dto.UserDto;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class DefaultUserOperation extends BaseOperation {
    private final TopicService topicService;
    private final UserService userService;
    private final UserStateService userStateService;
    private final FeedbackService feedbackService;

    public DefaultUserOperation(ActionBus actionBus,
                                TopicService topicService,
                                UserService userService,
                                UserStateService userStateService,
                                FeedbackService feedbackService) {
        super(actionBus);

        this.topicService = topicService;
        this.userService = userService;
        this.userStateService = userStateService;
        this.feedbackService = feedbackService;
    }

    public void execute(@Nonnull Action.ChannelType channel,
                        @Nonnull String channelUserId,
                        @Nonnull String text) {
        UserStateDto userState = userStateService.getUserStateByChannelAndChannelUserId(channel, channelUserId)
                .orElseThrow(() -> new UserActionException("Вы еще не зарегистрированы"));
        UserDto user = userService.getUserById(userState.getInnerUserId())
                .orElseThrow(() -> new UserActionException("Вы еще не зарегистрированы"));

        switch (userState.getStatus()) {
            case COLLECT_QUESTIONS:
                createQuestion(userState, user, text);
                break;
            case COLLECT_FEEDBACK:
                createFeedback(userState, user, text);
                break;
        }
    }

    private void createQuestion(UserStateDto userState, UserDto user, String questionText) {
        topicService.createQuestion(user.getId(), userState.getTopicId(), questionText)
                .orElseThrow(() -> new UserActionException("Я не знаю на каком вы докладе находитесь... Выберете его, пожалуйста"));

        sendTextMessageToUser(userState.getChannel(), userState.getChannelUserId(), "Я записал ваш вопрос. В конце доклада я передам все вопросы докладчику.");
    }

    private void createFeedback(UserStateDto userState, UserDto user, String feedbackText) {
        feedbackService.createFeedback(user.getId(), feedbackText);

        sendTextMessageToUser(userState.getChannel(), userState.getChannelUserId(), "Спасибо за отзыв! Я обязательно изучу его и постараюсь учесть все ваши замечания!");
    }
}