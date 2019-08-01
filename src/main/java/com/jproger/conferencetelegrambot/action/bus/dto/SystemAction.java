package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.Getter;

@Getter
public abstract class SystemAction extends Action {
    private String recipientId;

    public SystemAction(ChannelType channel, String recipientId) {
        super(channel);
        this.recipientId = recipientId;
    }
}
