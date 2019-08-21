package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.Getter;

@Getter
public class InitWorkflowUserAction extends UserAction {
    private String topic;

    public InitWorkflowUserAction(ChannelType channel, String externalUserId, String topic) {
        super(channel, externalUserId);
        
        this.topic = topic;
    }
}
