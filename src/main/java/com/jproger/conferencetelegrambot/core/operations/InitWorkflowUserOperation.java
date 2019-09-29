package com.jproger.conferencetelegrambot.core.operations;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseOperation;
import com.jproger.conferencetelegrambot.common.operations.exceptions.UserActionException;
import com.jproger.conferencetelegrambot.common.actions.Action.ChannelType;
import com.jproger.conferencetelegrambot.common.actions.RequestContactSystemAction;
import com.jproger.conferencetelegrambot.common.actions.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.topics.TopicService;
import com.jproger.conferencetelegrambot.topics.dto.TopicDto;
import com.jproger.conferencetelegrambot.topics.entities.Topic;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import com.jproger.conferencetelegrambot.workflow.entities.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@Slf4j
@Component
public class InitWorkflowUserOperation extends BaseOperation {
    private final UserStateService userStateService;
    private final TopicService topicService;

    public InitWorkflowUserOperation(ActionBus actionBus,
                                     UserStateService userStateService,
                                     TopicService topicService) {
        super(actionBus);

        this.userStateService = userStateService;
        this.topicService = topicService;
    }

    public void execute(@Nonnull ChannelType channel,
                        @Nonnull String channelUserId,
                        @Nullable Long topicId) {
        userStateService.getUserStateByChannelAndChannelUserId(channel, channelUserId)
                .orElseGet(() -> this.createUserState(channel, channelUserId));

        if (Objects.nonNull(topicId)) {
            TopicDto topic = topicService.getTopicById(topicId).orElse(null);

            validateTopicStatus(topic);

            updateTopic(channel, channelUserId, topic);
        }
    }

    private void updateTopic(ChannelType channel, String channelUserId, TopicDto topic) {
        userStateService.updateSelectedTopic(channel, channelUserId, topic.getId());

        userStateService.updateStatus(channel, channelUserId, Status.COLLECT_QUESTIONS);

        String message = String.format("Вы выбрали доклад - '%s'", topic.getName());

        sendMessage(channel, channelUserId, message);
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

    private UserStateDto createUserState(ChannelType channel, String channelUserId) {
        UserStateDto userState = userStateService.createUserState(channel, channelUserId);

        greetUser(channel, channelUserId);

        requestContactInfo(channel, channelUserId);

        return userState;
    }

    private void greetUser(ChannelType channel, String userId) {
        sendMessage(channel, userId, "Привет! Кажется, мы еще не знакомы... Меня зовут Добби.");
    }

    private void requestContactInfo(ChannelType channel, String userId) {
        RequestContactSystemAction action = new RequestContactSystemAction(channel, userId, "Поделишься со мной информацией о себе? Для этого нажми кнопку \"Зарегистрироваться\".");

        actionBus.sendAction(action);
    }

    private void sendMessage(ChannelType channelType, String userId, String message) {
        SendTextMessageSystemAction action = new SendTextMessageSystemAction(channelType, userId, message);

        actionBus.sendAction(action);
    }
}
