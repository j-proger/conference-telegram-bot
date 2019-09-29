package com.jproger.conferencetelegrambot.common.actions;

import lombok.Getter;

@Getter
public class RequestContactSystemAction extends SystemAction {
    private String message;

    public RequestContactSystemAction(ChannelType channel, String recipientId, String message) {
        super(channel, recipientId);

        this.message = message;
    }
}
