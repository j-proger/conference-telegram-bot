package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.Getter;

@Getter
public class StartUserAction extends UserAction {
    private String topic;

    public StartUserAction(ChannelType channel, String externalUserId, String topic) {
        super(channel, externalUserId);
        
        this.topic = topic;
    }
}
