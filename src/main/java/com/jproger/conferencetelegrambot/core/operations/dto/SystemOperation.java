package com.jproger.conferencetelegrambot.core.operations.dto;

import lombok.Getter;

@Getter
public abstract class SystemOperation extends Operation {
    public SystemOperation(ChannelType channel, String channelUserId) {
        super(channel, channelUserId);
    }
}
