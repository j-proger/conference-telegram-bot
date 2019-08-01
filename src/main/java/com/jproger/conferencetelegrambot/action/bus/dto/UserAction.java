package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.Getter;

@Getter
public abstract class UserAction extends Action {
    private String userId;

    public UserAction(ChannelType channel, String userId) {
        super(channel);
        this.userId = userId;
    }
}
