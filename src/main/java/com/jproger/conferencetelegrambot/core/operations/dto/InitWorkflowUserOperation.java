package com.jproger.conferencetelegrambot.core.operations.dto;

import lombok.Getter;

@Getter
public class InitWorkflowUserOperation extends UserOperation {
    private Long topicId;

    public InitWorkflowUserOperation(ChannelType channel, String externalUserId, Long topicId) {
        super(channel, externalUserId);
        
        this.topicId = topicId;
    }
}
