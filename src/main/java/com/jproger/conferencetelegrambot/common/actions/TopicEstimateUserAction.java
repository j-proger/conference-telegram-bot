package com.jproger.conferencetelegrambot.common.actions;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class TopicEstimateUserAction extends UserAction {
    private final long topicId;
    private final int rating;

    public TopicEstimateUserAction(ChannelType channel, String channelUserId, long topicId, int rating) {
        super(channel, channelUserId);
        this.topicId = topicId;
        this.rating = rating;
    }
}
