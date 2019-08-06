package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Action {
    public enum ChannelType {
        TELEGRAM
    }

    private ChannelType channel;
    private String channelUserId;
}
