package com.jproger.conferencetelegrambot.workflow.dto;

import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStateDto {
    private ChannelType channel;
    private String channelUserId;
    private Long innerUserId;
    private String topicKey;
}
