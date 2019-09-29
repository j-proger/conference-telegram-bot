package com.jproger.conferencetelegrambot.core.operations;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseOperation;
import com.jproger.conferencetelegrambot.common.actions.Action.ChannelType;
import com.jproger.conferencetelegrambot.common.actions.FinishRequestContactSystemAction;
import com.jproger.conferencetelegrambot.users.UserService;
import com.jproger.conferencetelegrambot.users.dto.UserDto;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.entities.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Objects;

@Slf4j
@Component
public class ShareContactUserOperation extends BaseOperation {
    private final UserService userService;
    private final UserStateService userStateService;

    public ShareContactUserOperation(ActionBus actionBus, UserService userService, UserStateService userStateService) {
        super(actionBus);

        this.userService = userService;
        this.userStateService = userStateService;
    }

    public void execute(@Nonnull ChannelType channel,
                        @Nonnull String channelUserId,
                        @Nonnull String phoneNumber,
                        @Nonnull String firstName,
                        @Nonnull String lastName) {
        UserDto user = createContact(firstName, lastName, phoneNumber);

        updateInnerUserIdInState(channel, channelUserId, user.getId());

        updateStateStatus(channel, channelUserId);

        finishRequestContact(channel, channelUserId);
    }

    private UserDto createContact(String firstName, String lastName, String phoneNumber) {
        UserDto userDto = UserDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .build();

        return userService.createUser(userDto);
    }

    private void updateInnerUserIdInState(ChannelType channel, String channelUserId, Long innerUserId) {
        userStateService.updateInnerUserId(channel, channelUserId, innerUserId);
    }

    private void updateStateStatus(ChannelType channel, String channelUserId) {
        userStateService.getUserStateByChannelAndChannelUserId(channel, channelUserId)
                .ifPresent(us -> {
                    Status newStatus = Objects.nonNull(us.getTopicId())
                            ? Status.COLLECT_QUESTIONS
                            : Status.REGISTERED;

                    userStateService.updateStatus(channel, channelUserId, newStatus);
                });
    }

    private void finishRequestContact(ChannelType channel, String userId) {
        FinishRequestContactSystemAction action = new FinishRequestContactSystemAction(channel, userId, "Спасибо! Теперь и я узнал тебя.");

        actionBus.sendAction(action);
    }
}
