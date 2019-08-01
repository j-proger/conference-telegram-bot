package com.jproger.conferencetelegrambot.action.bus.dto;

import lombok.Getter;

@Getter
public class MakeQuestionUserAction extends UserAction {
    private String question;

    public MakeQuestionUserAction(ChannelType channel, String userId, String question) {
        super(channel, userId);
        this.question = question;
    }
}
