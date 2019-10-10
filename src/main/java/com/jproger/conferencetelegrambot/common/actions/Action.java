package com.jproger.conferencetelegrambot.common.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public abstract class Action {
    public enum ChannelType {
        REST,
        TELEGRAM
    }

    private ChannelType channel;
    private String channelUserId;
}
