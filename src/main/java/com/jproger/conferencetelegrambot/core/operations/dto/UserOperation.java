package com.jproger.conferencetelegrambot.core.operations.dto;

import lombok.Getter;

@Getter
public abstract class UserOperation extends Operation {
    public UserOperation(ChannelType channel, String channelUserId) {
        super(channel, channelUserId);
    }
}
