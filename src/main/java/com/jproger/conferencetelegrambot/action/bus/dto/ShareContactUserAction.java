package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.Getter;

@Getter
public class ShareContactUserAction extends UserAction {
    private String phoneNumber;

    public ShareContactUserAction(ChannelType channel, String userId, String phoneNumber) {
        super(channel, userId);
        this.phoneNumber = phoneNumber;
    }
}
