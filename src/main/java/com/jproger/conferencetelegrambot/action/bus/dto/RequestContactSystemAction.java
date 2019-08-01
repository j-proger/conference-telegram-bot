package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.Getter;

@Getter
public class RequestContactSystemAction extends SystemAction {
    private String message;

    public RequestContactSystemAction(ChannelType channel, String recipientId, String message) {
        super(channel, recipientId);

        this.message = message;
    }
}
