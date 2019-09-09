package com.jproger.conferencetelegrambot.core.operations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Operation {
    public enum ChannelType {
        REST,
        TELEGRAM
    }

    private ChannelType channel;
    private String channelUserId;
}
