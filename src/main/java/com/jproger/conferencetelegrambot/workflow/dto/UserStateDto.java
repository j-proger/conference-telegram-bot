package com.jproger.conferencetelegrambot.workflow.dto;

import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.workflow.entities.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStateDto {
    private ChannelType channel;
    private String channelUserId;
    private Status status;
    private Long innerUserId;
    private String topicKey;
}
