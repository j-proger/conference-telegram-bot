package com.jproger.conferencetelegrambot.action.workflow.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class UserState {
    public enum Status {
        NEW,
        REGISTERED
    }

    /**
     * Id внутри компонента UserStateService
     */
    private Long id;
    /**
     * Id между компонентами
     */
    private Long externalId;
    private Status status;
    private List<UserChannel> userChannels;

    /**
     * Выбранный доклад
     */
    private String selectedTopic;

    public UserState copy() {
        return UserState.builder()
                .id(id)
                .externalId(externalId)
                .status(status)
                .selectedTopic(selectedTopic)
                .userChannels(copyChannels())
                .build();
    }

    private List<UserChannel> copyChannels() {
        return userChannels.stream()
                .map(UserChannel::copy)
                .collect(Collectors.toList());
    }
}
