package com.jproger.conferencetelegrambot.action.workflow.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.ActionConsumer;
import com.jproger.conferencetelegrambot.action.bus.dto.Action;
import com.jproger.conferencetelegrambot.action.bus.dto.FinishRequestContactSystemAction;
import com.jproger.conferencetelegrambot.action.bus.dto.ShareContactUserAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShareContactUserActionConsumer implements ActionConsumer {
    private final ActionBus actionBus;

    @PostConstruct
    public void registerInBus() {
        actionBus.registerConsumer(this);
    }

    @Override
    public Class<? extends Action> getActionClass() {
        return ShareContactUserAction.class;
    }

    @Override
    public void accept(Action a) {
        ShareContactUserAction action = (ShareContactUserAction) a;

        Action.ChannelType channel = action.getChannel();
        String userId = action.getUserId();
        String phoneNumber = action.getPhoneNumber();

        createContact(channel, userId, phoneNumber);

        finishRequestContact(channel, userId);
    }

    private void createContact(Action.ChannelType channel, String userId, String phoneNumber) {
        log.info("Create contact '{}' for user '{}'", phoneNumber, userId);
    }

    private void finishRequestContact(Action.ChannelType channel, String userId) {
        FinishRequestContactSystemAction action = new FinishRequestContactSystemAction(channel, userId, "Thank you for your phone number!");

        actionBus.sendAction(action);
    }
}
