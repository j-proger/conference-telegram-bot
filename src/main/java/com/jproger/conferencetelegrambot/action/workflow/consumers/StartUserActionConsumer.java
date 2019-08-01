package com.jproger.conferencetelegrambot.action.workflow.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.ActionConsumer;
import com.jproger.conferencetelegrambot.action.bus.dto.Action;
import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.action.bus.dto.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.action.bus.dto.StartUserAction;
import com.jproger.conferencetelegrambot.action.workflow.UserStateService;
import com.jproger.conferencetelegrambot.action.workflow.entities.UserState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Optional;

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

        UserState userState = userStateService.findUserState(action.getChannel(), action.getUserId())
                .orElseGet(() -> this.createUserState(action));

        if (Objects.nonNull(userState) && StringUtils.isNotBlank(action.getTopic())) {
            updateSelectedTopic(action, userState);
        }
    }

    private UserState createUserState(StartUserAction action) {
        Optional<UserState> userState = userStateService.createUserState(action.getChannel(), action.getUserId());

        userState.ifPresent(
                state -> sendMessage(action.getChannel(), action.getUserId(), "Hello, my dear friend. Before ask your questions, can I have your phone number please?")
        );

        return userState.orElse(null);
    }

    private void updateSelectedTopic(StartUserAction action, UserState userState) {
        userStateService.updateSelectedTopic(userState.getId(), action.getTopic())
                .ifPresent(state -> {
                    String message = String.format("You selected '%s' topic", action.getTopic());

                    sendMessage(action.getChannel(), action.getUserId(), message);
                });
    }

    private void sendMessage(ChannelType channelType, String userId, String message) {
        SendTextMessageSystemAction action = new SendTextMessageSystemAction(channelType, userId, message);

        actionBus.sendAction(action);
    }
}
