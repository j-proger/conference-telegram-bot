package com.jproger.conferencetelegrambot.common.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTopicEstimateSystemAction extends SystemAction {
    private final long topicId;

    public RequestTopicEstimateSystemAction(ChannelType channel, String channelUserId, long topicId) {
        super(channel, channelUserId);
        this.topicId = topicId;
    }
}
