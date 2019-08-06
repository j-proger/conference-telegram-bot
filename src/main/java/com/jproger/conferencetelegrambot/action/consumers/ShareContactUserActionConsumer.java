package com.jproger.conferencetelegrambot.action.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.ActionConsumer;
import com.jproger.conferencetelegrambot.action.bus.dto.Action;
import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.action.bus.dto.FinishRequestContactSystemAction;
import com.jproger.conferencetelegrambot.action.bus.dto.ShareContactUserAction;
import com.jproger.conferencetelegrambot.users.UserService;
import com.jproger.conferencetelegrambot.users.dto.UserDto;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShareContactUserActionConsumer implements ActionConsumer {
    private final ActionBus actionBus;
    private final UserService userService;
    private final UserStateService userStateService;

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

        UserDto user = createContact(action.getFirstName(), action.getLastName(), action.getMiddleName(), action.getPhoneNumber());

        updateInnerUserIdInState(action.getChannel(), action.getChannelUserId(), user.getId());

        finishRequestContact(action.getChannel(), action.getChannelUserId());
    }

    private UserDto createContact(String firstName, String lastName, String middleName, String phoneNumber) {
        UserDto userDto = UserDto.builder()
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .build();

        return userService.createUser(userDto);
    }

    private void updateInnerUserIdInState(ChannelType channel, String channelUserId, Long innerUserId) {
        userStateService.updateInnerUserId(channel, channelUserId, innerUserId);
    }

    private void finishRequestContact(ChannelType channel, String userId) {
        FinishRequestContactSystemAction action = new FinishRequestContactSystemAction(channel, userId, "Thank you for your phone number!");

        actionBus.sendAction(action);
    }
}
