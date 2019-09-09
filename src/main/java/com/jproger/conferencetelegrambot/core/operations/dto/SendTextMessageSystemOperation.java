package com.jproger.conferencetelegrambot.core.operations.dto;

import lombok.Getter;

@Getter
public class SendTextMessageSystemOperation extends SystemOperation {
    private String message;

    public SendTextMessageSystemOperation(ChannelType channel, String recipientId, String message) {
        super(channel, recipientId);
        this.message = message;
    }
}
