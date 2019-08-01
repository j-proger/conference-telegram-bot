package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.Getter;

@Getter
public class StartUserAction extends UserAction {
    private String topic;

    public StartUserAction(ChannelType channel, String userId, String topic) {
        super(channel, userId);
        this.topic = topic;
    }
}
