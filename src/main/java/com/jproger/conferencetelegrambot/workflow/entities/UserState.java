package com.jproger.conferencetelegrambot.workflow.entities;

import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserState {

    /**
     * Id внутри компонента UserStateService
     */
    private Long id;

    /**
     * Id внутри канала
     */
    private String channelUserId;

    /**
     * Канал взаимодействия
     */
    private ChannelType channel;

    /**
     * Id внутри сервиса Users
     */
    private Long innerUserId;

    /**
     * Выбранный доклад
     */
    private String topicKey;
}
