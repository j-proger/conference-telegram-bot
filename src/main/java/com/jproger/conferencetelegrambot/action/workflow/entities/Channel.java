package com.jproger.conferencetelegrambot.action.workflow.entities;

import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Channel {
    private ChannelType type;
    private String userId;

    public Channel copy() {
        return Channel.builder()
                .userId(userId)
                .type(type)
                .build();
    }
}
