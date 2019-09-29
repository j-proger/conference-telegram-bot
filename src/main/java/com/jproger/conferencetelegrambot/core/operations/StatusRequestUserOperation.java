package com.jproger.conferencetelegrambot.core.operations;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseOperation;
import com.jproger.conferencetelegrambot.common.actions.Action.ChannelType;
import com.jproger.conferencetelegrambot.common.actions.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import com.jproger.conferencetelegrambot.workflow.entities.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Slf4j
@Component
public class StatusRequestUserOperation extends BaseOperation {
    private final UserStateService userStateService;

    public StatusRequestUserOperation(ActionBus actionBus, UserStateService userStateService) {
        super(actionBus);

        this.userStateService = userStateService;
    }

    public void execute(@Nonnull ChannelType channel,
                           @Nonnull String channelUserId) {
        userStateService.getUserStateByChannelAndChannelUserId(channel, channelUserId)
                .map(UserStateDto::getStatus)
                .map(this::getStatusDescription)
                .ifPresent(d -> sendStatusDescriptionToUser(channel, channelUserId, d));
    }

    private void sendStatusDescriptionToUser(ChannelType channel, String channelUserId, String statusDescription) {
        SendTextMessageSystemAction action = new SendTextMessageSystemAction(channel, channelUserId, statusDescription);

        actionBus.sendAction(action);
    }

    private String getStatusDescription(Status status) {
        String description = "Происходит что-то непонятное...";

        switch (status) {
            case NEW:
                description = "Вы являетесь новым пользователем. Для продолжения работы вам необходимо зарегестрироваться. Для этого нажмите на кнопку \"Зарегистрироваться\" внизу экрана.";
                break;
            case REGISTERED:
                description = "Вы уже зарегистрировались. Теперь вы можете выбрать доклад и задавать на него вопросы. Для выбора доклада введите команду \"/start <topic>\" где topic - идентификатор доклада.";
                break;
            case COLLECT_QUESTIONS:
                description = "Доклад выбран. Теперь можно смело задавать вопросы. Для этого напишите любое текстовое сообщение мне.";
                break;
            case COLLECT_FEEDBACK:
                description = "Я хочу лучше работать и приносить больше пользы! Для этого мне необходимо узнать, в чем я был плох и что мне не хватает. Можете поделиться своим мнением?";
                break;
        }

        return description;
    }
}
