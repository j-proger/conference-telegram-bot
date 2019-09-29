package com.jproger.conferencetelegrambot.common.actions;

import lombok.Getter;

@Getter
public class FinishTopicSystemAction extends SystemAction {
    private final Long topicId;

    public FinishTopicSystemAction(ChannelType channel, String channelUserId, Long topicId) {
        super(channel, channelUserId);
        this.topicId = topicId;
    }
}
