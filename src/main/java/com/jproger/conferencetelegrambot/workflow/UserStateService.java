package com.jproger.conferencetelegrambot.workflow;

import com.jproger.conferencetelegrambot.common.actions.Action.ChannelType;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import com.jproger.conferencetelegrambot.workflow.entities.Status;
import com.jproger.conferencetelegrambot.workflow.entities.UserState;
import com.jproger.conferencetelegrambot.workflow.mappers.UserStateMapper;
import com.jproger.conferencetelegrambot.workflow.repositories.UserStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jproger.conferencetelegrambot.workflow.entities.Status.*;

@Service
@RequiredArgsConstructor
public class UserStateService {
    private final UserStateMapper userStateMapper;
    private final UserStateRepository userStateRepository;

    @Nonnull
    public Optional<UserStateDto> getUserStateByChannelAndChannelUserId(@Nonnull ChannelType channel, @Nonnull String channelUserId) {
        return userStateRepository.findByChannelAndChannelUserId(channel, channelUserId)
                .map(userStateMapper::toUserStateDto);
    }

    @Nonnull
    public UserStateDto createUserState(@Nonnull ChannelType channel, @Nonnull String channelUserId) {
        UserState state = UserState.builder()
                .channel(channel)
                .channelUserId(channelUserId)
                .status(NEW)
                .build();

        return userStateMapper.toUserStateDto(
                userStateRepository.save(state)
        );
    }

    @Nonnull
    public List<UserStateDto> getUserStatesSubscribedToTopic(long topicId) {
        return userStateRepository.findByTopicId(topicId)
                .stream()
                .map(userStateMapper::toUserStateDto)
                .collect(Collectors.toList());
    }

    public void clearTopicIdFromSubscribedUsers(long topicId) {
        userStateRepository.findByTopicId(topicId)
                .forEach(this::clearSelectedTopic);
    }

    public void updateSelectedTopic(@Nonnull ChannelType channel, @Nonnull String channelUserId, long topicId) {
        userStateRepository.findByChannelAndChannelUserId(channel, channelUserId)
                .ifPresent(state -> updateSelectedTopic(state, topicId));
    }

    public void updateInnerUserId(@Nonnull ChannelType channel, @Nonnull String channelUserId, long innerUserId) {
        userStateRepository.findByChannelAndChannelUserId(channel, channelUserId)
                .ifPresent(state -> updateInnerUserId(state, innerUserId));
    }

    public boolean updateStatus(@Nonnull ChannelType channel, @Nonnull String channelUserId, @Nonnull Status status) {
        return userStateRepository.findByChannelAndChannelUserId(channel, channelUserId)
                .map(us -> updateStatus(us, status))
                .orElse(false);
    }

    private void updateInnerUserId(UserState state, long innerUserId) {
        state.setInnerUserId(innerUserId);

        userStateRepository.save(state);
    }

    private void updateSelectedTopic(UserState state, long topicId) {
        state.setTopicId(topicId);

        userStateRepository.save(state);
    }

    private void clearSelectedTopic(UserState state) {
        state.setTopicId(null);

        userStateRepository.save(state);
    }

    private boolean updateStatus(UserState state, Status status) {
        if (isAllowChangeStatus(state, status)) {
            state.setStatus(status);

            userStateRepository.save(state);

            return true;
        }

        return false;
    }

    private boolean isAllowChangeStatus(UserState state, Status to) {
        Status from = state.getStatus();

        switch (to) {
            case NEW:
                return false;
            case REGISTERED:
                return Arrays.asList(COLLECT_QUESTIONS, NEW)
                        .contains(from);
            case COLLECT_QUESTIONS:
                return Arrays.asList(NEW, REGISTERED).contains(from)
                        && Objects.nonNull(state.getTopicId())
                        && Objects.nonNull(state.getInnerUserId());
            case COLLECT_FEEDBACK:
                return Arrays.asList(REGISTERED, COLLECT_QUESTIONS)
                        .contains(from);
        }

        return false;
    }
}
