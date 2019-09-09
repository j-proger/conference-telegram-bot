package com.jproger.conferencetelegrambot.core.operations.executors;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseOperationExecutor;
import com.jproger.conferencetelegrambot.core.operations.dto.FinishRequestContactSystemOperation;
import com.jproger.conferencetelegrambot.core.operations.dto.Operation.ChannelType;
import com.jproger.conferencetelegrambot.core.operations.dto.ShareContactUserOperation;
import com.jproger.conferencetelegrambot.users.UserService;
import com.jproger.conferencetelegrambot.users.dto.UserDto;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.entities.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class ShareContactUserOperationExecutor extends BaseOperationExecutor<ShareContactUserOperation> {
    private final UserService userService;
    private final UserStateService userStateService;

    public ShareContactUserOperationExecutor(ActionBus actionBus, UserService userService, UserStateService userStateService) {
        super(ShareContactUserOperation.class, actionBus);

        this.userService = userService;
        this.userStateService = userStateService;
    }

    @Override
    public void acceptTAction(ShareContactUserOperation action) {
        UserDto user = createContact(action.getFirstName(), action.getLastName(), action.getMiddleName(), action.getPhoneNumber());

        updateInnerUserIdInState(action.getChannel(), action.getChannelUserId(), user.getId());

        updateStateStatus(action.getChannel(), action.getChannelUserId());

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
        FinishRequestContactSystemOperation action = new FinishRequestContactSystemOperation(channel, userId, "Спасибо! Теперь и я узнал тебя.");

        actionBus.sendAction(action);
    }
}
