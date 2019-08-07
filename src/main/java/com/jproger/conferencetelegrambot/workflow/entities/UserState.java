package com.jproger.conferencetelegrambot.workflow.entities;

import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserState {

    /**
     * Id внутри компонента UserStateService
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Id внутри канала
     */
    private String channelUserId;

    /**
     * Канал взаимодействия
     */
    @Enumerated(EnumType.STRING)
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

