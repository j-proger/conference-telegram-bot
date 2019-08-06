package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.Getter;

@Getter
public class MakeQuestionUserAction extends UserAction {
    private String question;

    public MakeQuestionUserAction(ChannelType channel, String externalUserId, String question) {
        super(channel, externalUserId);
        this.question = question;
    }
}
