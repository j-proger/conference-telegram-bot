package com.jproger.conferencetelegrambot.action.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.action.bus.dto.RequestContactSystemAction;
import com.jproger.conferencetelegrambot.action.bus.dto.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.action.bus.dto.StartUserAction;
import com.jproger.conferencetelegrambot.action.consumers.exceptions.UserActionException;
import com.jproger.conferencetelegrambot.topics.TopicService;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartUserActionConsumer extends BaseActionConsumer<StartUserAction> {
    private final UserStateService userStateService;
    private final TopicService topicService;

    public StartUserActionConsumer(ActionBus actionBus,
                                   UserStateService userStateService,
                                   TopicService topicService) {
        super(StartUserAction.class, actionBus);

        this.userStateService = userStateService;
        this.topicService = topicService;
    }

    @Override
    public void acceptTAction(StartUserAction action) {
        userStateService.getUserStateByChannelAndChannelUserId(action.getChannel(), action.getChannelUserId())
                .orElseGet(() -> this.createUserState(action));

        if (StringUtils.isNotBlank(action.getTopic())) {
            if (!isTopicExists(action.getTopic())) {
                throw new UserActionException("You sent wrong topic. Try again with another topic.");
            }

            updateTopicKey(action);
        }
    }

    private boolean isTopicExists(String topicKey) {
        return topicService.getTopicByKey(topicKey).isPresent();
    }

    private void updateTopicKey(StartUserAction action) {
        userStateService.updateTopicKey(action.getChannel(), action.getChannelUserId(), action.getTopic());

        String message = String.format("You selected '%s' topic", action.getTopic());

        sendMessage(action.getChannel(), action.getChannelUserId(), message);
    }

    private UserStateDto createUserState(StartUserAction action) {
        UserStateDto userState = userStateService.createUserState(action.getChannel(), action.getChannelUserId());

        greetUser(action.getChannel(), action.getChannelUserId());

        requestContactInfo(action.getChannel(), action.getChannelUserId());

        return userState;
    }

    private void greetUser(ChannelType channel, String userId) {
        sendMessage(channel, userId, "Hello, my dear friend.");
    }

    private void requestContactInfo(ChannelType channel, String userId) {
        RequestContactSystemAction action = new RequestContactSystemAction(channel, userId, "Can I have your phone number, please?");

        actionBus.sendAction(action);
    }

    private void sendMessage(ChannelType channelType, String userId, String message) {
        SendTextMessageSystemAction action = new SendTextMessageSystemAction(channelType, userId, message);

        actionBus.sendAction(action);
    }
}
