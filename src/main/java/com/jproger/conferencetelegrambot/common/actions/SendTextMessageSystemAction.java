package com.jproger.conferencetelegrambot.common.actions;

import lombok.Getter;

@Getter
public class SendTextMessageSystemAction extends SystemAction {
    private String message;

    public SendTextMessageSystemAction(ChannelType channel, String recipientId, String message) {
        super(channel, recipientId);
        this.message = message;
    }
}
