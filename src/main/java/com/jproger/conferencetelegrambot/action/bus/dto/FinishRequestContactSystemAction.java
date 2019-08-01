package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.Getter;

@Getter
public class FinishRequestContactSystemAction extends SystemAction {
    private String message;

    public FinishRequestContactSystemAction(ChannelType channel, String recipientId, String message) {
        super(channel, recipientId);

        this.message = message;
    }
}
