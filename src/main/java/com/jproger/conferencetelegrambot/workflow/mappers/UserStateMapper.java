package com.jproger.conferencetelegrambot.workflow.mappers;

import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import com.jproger.conferencetelegrambot.workflow.entities.UserState;
import org.springframework.stereotype.Component;

@Component
public class UserStateMapper {
    public UserStateDto toUserStateDto(UserState state) {
        return UserStateDto.builder()
                .channel(state.getChannel())
                .channelUserId(state.getChannelUserId())
                .status(state.getStatus())
                .innerUserId(state.getInnerUserId())
                .topicId(state.getTopicId())
                .build();
    }
}
