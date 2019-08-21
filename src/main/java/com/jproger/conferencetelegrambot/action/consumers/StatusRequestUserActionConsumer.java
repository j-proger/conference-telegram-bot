package com.jproger.conferencetelegrambot.action.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.action.bus.dto.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.action.bus.dto.StatusRequestUserAction;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import com.jproger.conferencetelegrambot.workflow.entities.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StatusRequestUserActionConsumer extends BaseActionConsumer<StatusRequestUserAction> {
    private final UserStateService userStateService;

    public StatusRequestUserActionConsumer(ActionBus actionBus, UserStateService userStateService) {
        super(StatusRequestUserAction.class, actionBus);

        this.userStateService = userStateService;
    }

    @Override
    protected void acceptTAction(StatusRequestUserAction action) {
        ChannelType channel = action.getChannel();
        String channelUserId = action.getChannelUserId();

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
        String description = "<UNKNOWN>";

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
