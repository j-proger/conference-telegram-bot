package com.jproger.conferencetelegrambot.action.workflow;

import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.action.workflow.entities.UserChannel;
import com.jproger.conferencetelegrambot.action.workflow.entities.UserState;
import com.jproger.conferencetelegrambot.action.workflow.entities.UserState.Status;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

@Service
public class UserStateService {
    private static final AtomicLong idGenerator = new AtomicLong(0);
    private final List<UserState> userStates = new ArrayList<>();

    @Nonnull
    public Optional<UserState> findUserState(@Nonnull ChannelType channel, @Nonnull String userId) {
        return userStates.stream()
                .filter(getUserStatePredicateByChannelTypeAndUserId(channel, userId))
                .findFirst()
                .map(UserState::copy);
    }

    @Nonnull
    public Optional<UserState> createUserState(@Nonnull ChannelType channel, @Nonnull String userId) {
        UserChannel userChannel = UserChannel.builder()
                .channelType(channel)
                .userId(userId)
                .build();

        UserState userState = UserState.builder()
                .id(idGenerator.addAndGet(1))
                .status(Status.NEW)
                .userChannels(
                        Collections.singletonList(userChannel)
                ).build();

        userStates.add(userState);

        return Optional.of(userState)
                .map(UserState::copy);
    }

    @Nonnull
    public Optional<UserState> updateSelectedTopic(@Nonnull Long id, @Nonnull String topic) {
        UserState userState = findUserState(id);

        if (Objects.nonNull(userState)) {
            userState.setSelectedTopic(topic);
        }

        return Optional.ofNullable(userState)
                .map(UserState::copy);
    }

    private UserState findUserState(Long id) {
        return userStates.stream()
                .filter(getUserStatePredicateById(id))
                .findFirst().orElse(null);
    }

    private Predicate<? super UserState> getUserStatePredicateByChannelTypeAndUserId(ChannelType channel, String userId) {
        return us -> us.getUserChannels().stream().anyMatch(getUserChannelPredicateBuChannelTypeAndUserId(channel, userId));
    }

    private Predicate<? super UserState> getUserStatePredicateById(Long id) {
        return us -> us.getId().equals(id);
    }

    private Predicate<? super UserChannel> getUserChannelPredicateBuChannelTypeAndUserId(ChannelType channel, String userId) {
        return uc -> uc.getUserId().equals(userId) && uc.getChannelType().equals(channel);
    }
}
