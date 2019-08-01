package com.jproger.conferencetelegrambot.action.workflow.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.ActionConsumer;
import com.jproger.conferencetelegrambot.action.bus.dto.Action;
import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.action.bus.dto.RequestContactSystemAction;
import com.jproger.conferencetelegrambot.action.bus.dto.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.action.bus.dto.StartUserAction;
import com.jproger.conferencetelegrambot.action.workflow.WorkflowStateService;
import com.jproger.conferencetelegrambot.action.workflow.entities.WorkflowState;
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
    private final WorkflowStateService workflowStateService;

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

        WorkflowState workflowState = workflowStateService.findUserState(action.getChannel(), action.getUserId())
                .orElseGet(() -> this.createUserState(action));

        if (Objects.nonNull(workflowState) && StringUtils.isNotBlank(action.getTopic())) {
            updateSelectedTopic(action, workflowState);
        }
    }

    private void updateSelectedTopic(StartUserAction action, WorkflowState workflowState) {
        workflowStateService.updateSelectedTopic(workflowState.getId(), action.getTopic())
                .ifPresent(state -> {
                    String message = String.format("You selected '%s' topic", action.getTopic());

                    sendMessage(action.getChannel(), action.getUserId(), message);
                });
    }

    private WorkflowState createUserState(StartUserAction action) {
        Optional<WorkflowState> userState = workflowStateService.createUserState(action.getChannel(), action.getUserId());

        userState.ifPresent(
                state -> greetUser(action.getChannel(), action.getUserId())
        );

        return userState.orElse(null);
    }

    private void greetUser(ChannelType channel, String userId) {
        sendMessage(channel, userId, "Hello, my dear friend.");

        requestContactInfo(channel, userId);
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
