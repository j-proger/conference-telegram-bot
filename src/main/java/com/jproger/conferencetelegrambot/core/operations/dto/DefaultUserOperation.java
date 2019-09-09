package com.jproger.conferencetelegrambot.core.operations.dto;

import lombok.Getter;

@Getter
public class DefaultUserOperation extends UserOperation {
    private String text;

    public DefaultUserOperation(ChannelType channel, String externalUserId, String text) {
        super(channel, externalUserId);
        this.text = text;
    }
}
