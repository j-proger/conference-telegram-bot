package com.jproger.conferencetelegrambot.action.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.action.bus.dto.RequestContactSystemAction;
import com.jproger.conferencetelegrambot.action.bus.dto.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.action.bus.dto.InitWorkflowUserAction;
import com.jproger.conferencetelegrambot.action.consumers.exceptions.UserActionException;
import com.jproger.conferencetelegrambot.topics.TopicService;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitWorkflowUserActionConsumer extends BaseActionConsumer<InitWorkflowUserAction> {
    private final UserStateService userStateService;
    private final TopicService topicService;

    public InitWorkflowUserActionConsumer(ActionBus actionBus,
                                          UserStateService userStateService,
                                          TopicService topicService) {
        super(InitWorkflowUserAction.class, actionBus);

        this.userStateService = userStateService;
        this.topicService = topicService;
    }

    @Override
    public void acceptTAction(InitWorkflowUserAction action) {
        userStateService.getUserStateByChannelAndChannelUserId(action.getChannel(), action.getChannelUserId())
                .orElseGet(() -> this.createUserState(action));

        if (StringUtils.isNotBlank(action.getTopic())) {
            if (!isTopicExists(action.getTopic())) {
                throw new UserActionException("");
            }

            updateTopicKey(action);
        }
    }

    private boolean isTopicExists(String topicKey) {
        return topicService.getTopicByKey(topicKey).isPresent();
    }

    private void updateTopicKey(InitWorkflowUserAction action) {
        userStateService.updateTopicKey(action.getChannel(), action.getChannelUserId(), action.getTopic());

        String message = String.format("Я запомнил ваш выбор доклада - '%s'", action.getTopic());

        sendMessage(action.getChannel(), action.getChannelUserId(), message);
    }

    private UserStateDto createUserState(InitWorkflowUserAction action) {
        UserStateDto userState = userStateService.createUserState(action.getChannel(), action.getChannelUserId());

        greetUser(action.getChannel(), action.getChannelUserId());

        requestContactInfo(action.getChannel(), action.getChannelUserId());

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
