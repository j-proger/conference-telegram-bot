package com.jproger.conferencetelegrambot.action.workflow.entities;

import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChannel {
    private ChannelType channelType;
    private String userId;

    public UserChannel copy() {
        return UserChannel.builder()
                .userId(userId)
                .channelType(channelType)
                .build();
    }
}
