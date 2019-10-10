package com.jproger.conferencetelegrambot.common.actions;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DefaultUserAction extends UserAction {
    private String text;

    public DefaultUserAction(ChannelType channel, String externalUserId, String text) {
        super(channel, externalUserId);
        this.text = text;
    }
}
