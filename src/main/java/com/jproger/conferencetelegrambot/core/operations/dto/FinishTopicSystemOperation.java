package com.jproger.conferencetelegrambot.core.operations.dto;

import lombok.Getter;

@Getter
public class FinishTopicSystemOperation extends SystemOperation {
    private final Long topicId;

    public FinishTopicSystemOperation(ChannelType channel, String channelUserId, Long topicId) {
        super(channel, channelUserId);
        this.topicId = topicId;
    }
}
