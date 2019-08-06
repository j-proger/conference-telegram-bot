package com.jproger.conferencetelegrambot.action.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.ActionConsumer;
import com.jproger.conferencetelegrambot.action.bus.dto.Action;
import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.action.bus.dto.RequestContactSystemAction;
import com.jproger.conferencetelegrambot.action.bus.dto.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.action.bus.dto.StartUserAction;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartUserActionConsumer implements ActionConsumer {
    private final ActionBus actionBus;
    private final UserStateService userStateService;

    @PostConstruct
    public void registerInBus() {
        actionBus.registerConsumer(this);
    }

    @Override
    public Class<? extends Action> getActionClass() {
        return StartUserAction.class;
    }

    @Override
    public void accept(Action a) {
        StartUserAction action = (StartUserAction) a;

        userStateService.getUserStateByChannelAndChannelUserId(action.getChannel(), action.getChannelUserId())
                .orElseGet(() -> this.createUserState(action));

        if (StringUtils.isNotBlank(action.getTopic())) {
            updateTopicKey(action);
        }
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
