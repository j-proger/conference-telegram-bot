package com.jproger.conferencetelegrambot.workflow.mappers;

import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import com.jproger.conferencetelegrambot.workflow.entities.UserState;
import org.springframework.stereotype.Component;

@Component
public class UserStateMapper {
    public UserStateDto toUserStateDtoMap(UserState state) {
        return UserStateDto.builder()
                .channel(state.getChannel())
                .channelUserId(state.getChannelUserId())
                .innerUserId(state.getInnerUserId())
                .topicKey(state.getTopicKey())
                .build();
    }
}
