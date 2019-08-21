package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.Getter;

@Getter
public class DefaultUserAction extends UserAction {
    private String question;

    public DefaultUserAction(ChannelType channel, String externalUserId, String question) {
        super(channel, externalUserId);
        this.question = question;
    }
}
