package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.Getter;

@Getter
public abstract class SystemAction extends Action {
    public SystemAction(ChannelType channel, String channelUserId) {
        super(channel, channelUserId);
    }
}
