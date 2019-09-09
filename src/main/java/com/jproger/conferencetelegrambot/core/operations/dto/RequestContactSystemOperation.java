package com.jproger.conferencetelegrambot.core.operations.dto;

import lombok.Getter;

@Getter
public class RequestContactSystemOperation extends SystemOperation {
    private String message;

    public RequestContactSystemOperation(ChannelType channel, String recipientId, String message) {
        super(channel, recipientId);

        this.message = message;
    }
}
