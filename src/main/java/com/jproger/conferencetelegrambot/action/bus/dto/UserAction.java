package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.Getter;

@Getter
public abstract class UserAction extends Action {
    public UserAction(ChannelType channel, String channelUserId) {
        super(channel, channelUserId);
    }
}
