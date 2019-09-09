package com.jproger.conferencetelegrambot.core.operations.executors;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseOperationExecutor;
import com.jproger.conferencetelegrambot.common.operations.exceptions.UserActionException;
import com.jproger.conferencetelegrambot.core.operations.dto.InitWorkflowUserOperation;
import com.jproger.conferencetelegrambot.core.operations.dto.Operation;
import com.jproger.conferencetelegrambot.core.operations.dto.RequestContactSystemOperation;
import com.jproger.conferencetelegrambot.core.operations.dto.SendTextMessageSystemOperation;
import com.jproger.conferencetelegrambot.topics.TopicService;
import com.jproger.conferencetelegrambot.topics.dto.TopicDto;
import com.jproger.conferencetelegrambot.topics.entities.Topic;
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
            TopicDto topic = topicService.getTopicById(operation.getTopicId()).orElse(null);

            validateTopicStatus(topic);

            updateTopic(operation, topic);
        }
    }

    private void updateTopic(InitWorkflowUserOperation operation, TopicDto topic) {
        userStateService.updateSelectedTopic(operation.getChannel(), operation.getChannelUserId(), topic.getId());

        userStateService.updateStatus(operation.getChannel(), operation.getChannelUserId(), Status.COLLECT_QUESTIONS);

        String message = String.format("Вы выбрали доклад - '%s'", topic.getName());

        sendMessage(operation.getChannel(), operation.getChannelUserId(), message);
    }

    private void validateTopicStatus(TopicDto topic) {
        if (Objects.isNull(topic)) {
            throw new UserActionException("Доклад не найден");
        } else if (topic.getStatus() == Topic.Status.CREATED) {
            throw new UserActionException("Доклад еще не начался");
        } else if (topic.getStatus() == Topic.Status.FINISHED) {
            throw new UserActionException("Доклад уже завершился");
        }
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
