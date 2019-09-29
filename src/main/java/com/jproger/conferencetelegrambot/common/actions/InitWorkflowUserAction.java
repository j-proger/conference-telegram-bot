package com.jproger.conferencetelegrambot.common.actions;

import lombok.Getter;

@Getter
public class InitWorkflowUserAction extends UserAction {
    private Long topicId;

    public InitWorkflowUserAction(ChannelType channel, String externalUserId, Long topicId) {
        super(channel, externalUserId);
        
        this.topicId = topicId;
    }
}
