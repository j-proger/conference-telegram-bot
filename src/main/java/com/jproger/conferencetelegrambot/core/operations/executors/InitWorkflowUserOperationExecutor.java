package com.jproger.conferencetelegrambot.core.operations.executors;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseOperationExecutor;
import com.jproger.conferencetelegrambot.core.operations.dto.InitWorkflowUserOperation;
import com.jproger.conferencetelegrambot.core.operations.dto.Operation;
import com.jproger.conferencetelegrambot.core.operations.dto.RequestContactSystemOperation;
import com.jproger.conferencetelegrambot.core.operations.dto.SendTextMessageSystemOperation;
import com.jproger.conferencetelegrambot.common.operations.exceptions.UserActionException;
import com.jproger.conferencetelegrambot.topics.TopicService;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import com.jproger.conferencetelegrambot.workflow.entities.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class InitWorkflowUserOperationExecutor extends BaseOperationExecutor<InitWorkflowUserOperation> {
    private final UserStateService userStateService;
    private final TopicService topicService;

    public InitWorkflowUserOperationExecutor(ActionBus actionBus,
                                             UserStateService userStateService,
                                             TopicService topicService) {
        super(InitWorkflowUserOperation.class, actionBus);

        this.userStateService = userStateService;
        this.topicService = topicService;
    }

    @Override
    public void acceptTAction(InitWorkflowUserOperation operation) {
        userStateService.getUserStateByChannelAndChannelUserId(operation.getChannel(), operation.getChannelUserId())
                .orElseGet(() -> this.createUserState(operation));

        if (Objects.nonNull(operation.getTopicId())) {
            if (!isTopicExists(operation.getTopicId())) {
                throw new UserActionException("Доклад не найден :(");
            }

            updateTopic(operation);
        }
    }

    private boolean isTopicExists(Long topicId) {
        return topicService.getTopicById(topicId).isPresent();
    }

    private void updateTopic(InitWorkflowUserOperation operation) {
        userStateService.updateSelectedTopic(operation.getChannel(), operation.getChannelUserId(), operation.getTopicId());

        userStateService.updateStatus(operation.getChannel(), operation.getChannelUserId(), Status.COLLECT_QUESTIONS);

        String message = String.format("Я запомнил ваш выбор доклада - '%d'", operation.getTopicId());

        sendMessage(operation.getChannel(), operation.getChannelUserId(), message);
    }

    private UserStateDto createUserState(InitWorkflowUserOperation action) {
        UserStateDto userState = userStateService.createUserState(action.getChannel(), action.getChannelUserId());

        greetUser(action.getChannel(), action.getChannelUserId());

        requestContactInfo(action.getChannel(), action.getChannelUserId());

        return userState;
    }

    private void greetUser(Operation.ChannelType channel, String userId) {
        sendMessage(channel, userId, "Привет! Кажется, мы еще не знакомы... Меня зовут Добби.");
    }

    private void requestContactInfo(Operation.ChannelType channel, String userId) {
        RequestContactSystemOperation action = new RequestContactSystemOperation(channel, userId, "Поделишься со мной информацией о себе? Для этого нажми кнопку \"Зарегистрироваться\".");

        actionBus.sendAction(action);
    }

    private void sendMessage(Operation.ChannelType channelType, String userId, String message) {
        SendTextMessageSystemOperation action = new SendTextMessageSystemOperation(channelType, userId, message);

        actionBus.sendAction(action);
    }
}
