package com.jproger.conferencetelegrambot.workflow;

import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import com.jproger.conferencetelegrambot.workflow.entities.Status;
import com.jproger.conferencetelegrambot.workflow.entities.UserState;
import com.jproger.conferencetelegrambot.workflow.mappers.UserStateMapper;
import com.jproger.conferencetelegrambot.workflow.repositories.UserStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserStateService {
    private final UserStateMapper userStateMapper;
    private final UserStateRepository userStateRepository;

    @Nonnull
    public Optional<UserStateDto> getUserStateByChannelAndChannelUserId(@Nonnull ChannelType channel, @Nonnull String channelUserId) {
        return userStateRepository.findByChannelAndChannelUserId(channel, channelUserId)
                .map(userStateMapper::toUserStateDtoMap);
    }

    @Nonnull
    public UserStateDto createUserState(@Nonnull ChannelType channel, @Nonnull String channelUserId) {
        UserState state = UserState.builder()
                .channel(channel)
                .channelUserId(channelUserId)
                .status(Status.NEW)
                .build();

        return userStateMapper.toUserStateDtoMap(
                userStateRepository.save(state)
        );
    }

    public void updateTopicKey(@Nonnull ChannelType channel, @Nonnull String channelUserId, @Nonnull String topicKey) {
        userStateRepository.findByChannelAndChannelUserId(channel, channelUserId)
                .ifPresent(state -> updateTopicKey(state, topicKey));
    }

    public void updateInnerUserId(@Nonnull ChannelType channel, @Nonnull String channelUserId, long innerUserId) {
        userStateRepository.findByChannelAndChannelUserId(channel, channelUserId)
                .ifPresent(state -> updateInnerUserId(state, innerUserId));
    }

    private void updateInnerUserId(UserState state, long innerUserId) {
        state.setInnerUserId(innerUserId);
        state.setStatus(Status.REGISTERED);

        userStateRepository.save(state);
    }

    private void updateTopicKey(UserState state, String topicKey) {
        state.setTopicKey(topicKey);
        state.setStatus(Status.COLLECT_QUESTIONS);

        userStateRepository.save(state);
    }
}
