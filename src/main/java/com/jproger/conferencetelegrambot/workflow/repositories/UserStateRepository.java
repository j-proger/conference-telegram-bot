package com.jproger.conferencetelegrambot.workflow.repositories;

import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.workflow.entities.UserState;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class UserStateRepository {
    private static long currentId = 0;

    private final Map<Long, UserState> statesMap = new HashMap<>();

    public UserState save(UserState state) {
        if (Objects.isNull(state.getId())) {
            state.setId(nextId());
        }

        statesMap.put(state.getId(), state);

        return statesMap.get(state.getId());
    }

    public Optional<UserState> findById(long id) {
        return statesMap.values().stream()
                .filter(getUserStateByIdPredicate(id))
                .findFirst();
    }

    public Optional<UserState> findByChannelAndChannelUserId(@Nonnull ChannelType channel, @Nonnull String channelUserId) {
        return statesMap.values().stream()
                .filter(getUserStateByChannelAndChannelUserIdPredicate(channel, channelUserId))
                .findFirst();
    }

    public Optional<UserState> findByInnerUserId(long innerUserId) {
        return statesMap.values().stream()
                .filter(getUserStateByInnerUserIdPredicate(innerUserId))
                .findFirst();
    }

    private long nextId() {
        return ++currentId;
    }

    private Predicate<? super UserState> getUserStateByIdPredicate(long id) {
        return state -> state.getId().equals(id);
    }

    private Predicate<? super UserState> getUserStateByChannelAndChannelUserIdPredicate(ChannelType channel, String channelUserId) {
        return state -> state.getChannel() == channel && state.getChannelUserId().equals(channelUserId);
    }

    private Predicate<? super UserState> getUserStateByInnerUserIdPredicate(long id) {
        return state -> state.getId().equals(id);
    }
}
