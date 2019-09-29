package com.jproger.conferencetelegrambot.common.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Action {
    public enum ChannelType {
        REST,
        TELEGRAM
    }

    private ChannelType channel;
    private String channelUserId;
}
