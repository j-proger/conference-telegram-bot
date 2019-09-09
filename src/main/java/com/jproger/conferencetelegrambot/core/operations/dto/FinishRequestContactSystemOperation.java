package com.jproger.conferencetelegrambot.core.operations.dto;

import lombok.Getter;

@Getter
public class FinishRequestContactSystemOperation extends SystemOperation {
    private String message;

    public FinishRequestContactSystemOperation(ChannelType channel, String recipientId, String message) {
        super(channel, recipientId);

        this.message = message;
    }
}
