package com.jproger.conferencetelegrambot.action.workflow.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class WorkflowState {
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
    private List<Channel> channels;

    /**
     * Выбранный доклад
     */
    private String selectedTopic;

    public WorkflowState copy() {
        return WorkflowState.builder()
                .id(id)
                .externalId(externalId)
                .status(status)
                .selectedTopic(selectedTopic)
                .userChannels(copyChannels())
                .build();
    }

    private List<Channel> copyChannels() {
        return channels.stream()
                .map(Channel::copy)
                .collect(Collectors.toList());
    }
}
