package com.jproger.conferencetelegrambot.action.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.action.bus.dto.FinishRequestContactSystemAction;
import com.jproger.conferencetelegrambot.action.bus.dto.ShareContactUserAction;
import com.jproger.conferencetelegrambot.users.UserService;
import com.jproger.conferencetelegrambot.users.dto.UserDto;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShareContactUserActionConsumer extends BaseActionConsumer<ShareContactUserAction> {
    private final UserService userService;
    private final UserStateService userStateService;

    public ShareContactUserActionConsumer(ActionBus actionBus, UserService userService, UserStateService userStateService) {
        super(ShareContactUserAction.class, actionBus);

        this.userService = userService;
        this.userStateService = userStateService;
    }

    @Override
    public void acceptTAction(ShareContactUserAction action) {
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
