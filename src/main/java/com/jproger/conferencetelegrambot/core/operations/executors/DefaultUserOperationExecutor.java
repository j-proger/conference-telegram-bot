package com.jproger.conferencetelegrambot.core.operations.executors;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseOperationExecutor;
import com.jproger.conferencetelegrambot.common.operations.exceptions.UserActionException;
import com.jproger.conferencetelegrambot.core.operations.dto.DefaultUserOperation;
import com.jproger.conferencetelegrambot.core.operations.dto.Operation;
import com.jproger.conferencetelegrambot.core.operations.dto.RequestContactSystemOperation;
import com.jproger.conferencetelegrambot.feedback.FeedbackService;
import com.jproger.conferencetelegrambot.topics.TopicService;
import com.jproger.conferencetelegrambot.users.UserService;
import com.jproger.conferencetelegrambot.users.dto.UserDto;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultUserOperationExecutor extends BaseOperationExecutor<DefaultUserOperation> {
    private final TopicService topicService;
    private final UserService userService;
    private final UserStateService userStateService;
    private final FeedbackService feedbackService;

    public DefaultUserOperationExecutor(ActionBus actionBus,
                                        TopicService topicService,
                                        UserService userService,
                                        UserStateService userStateService,
                                        FeedbackService feedbackService) {
        super(DefaultUserOperation.class, actionBus);

        this.topicService = topicService;
        this.userService = userService;
        this.userStateService = userStateService;
        this.feedbackService = feedbackService;
    }

    @Override
    protected void acceptTAction(DefaultUserOperation operation) {
        Optional<UserStateDto> userState = getUserStateByAction(operation);
        Optional<UserDto> user = getUserByUserState(
                userState.map(UserStateDto::getInnerUserId)
                        .orElse(null)
        );

        if (userState.isPresent() && user.isPresent()) {
            handleOperation(operation, userState.get(), user.get());
        } else {
            requestRegisterUser(operation.getChannel(), operation.getChannelUserId());
        }
    }

    private Optional<UserStateDto> getUserStateByAction(DefaultUserOperation action) {
        return userStateService.getUserStateByChannelAndChannelUserId(action.getChannel(), action.getChannelUserId());
    }

    private Optional<UserDto> getUserByUserState(Long userId) {
        return Optional.ofNullable(userId)
                .flatMap(userService::getUserById);
    }

    private void handleOperation(DefaultUserOperation operation, UserStateDto userState, UserDto user) {
        switch (userState.getStatus()) {
            case COLLECT_QUESTIONS:
                createQuestion(userState, user, operation.getText());
                break;
            case COLLECT_FEEDBACK:
                createFeedback(userState, user, operation.getText());
                break;
        }
    }

    private void requestRegisterUser(Operation.ChannelType channel, String channelUserId) {
        RequestContactSystemOperation action = new RequestContactSystemOperation(channel, channelUserId, "Я вас еще не знаю... Зарегистрируйтесь, пожалуйста.");

        actionBus.sendAction(action);
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