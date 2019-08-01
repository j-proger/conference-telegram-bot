package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.Getter;

@Getter
public class RequestContactSystemAction extends SystemAction {
    public RequestContactSystemAction(ChannelType channel, String recipientId) {
        super(channel, recipientId);
    }
}
