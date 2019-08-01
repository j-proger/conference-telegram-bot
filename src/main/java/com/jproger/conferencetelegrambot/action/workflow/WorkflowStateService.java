package com.jproger.conferencetelegrambot.action.workflow;

import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.action.workflow.entities.Channel;
import com.jproger.conferencetelegrambot.action.workflow.entities.WorkflowState;
import com.jproger.conferencetelegrambot.action.workflow.entities.WorkflowState.Status;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

@Service
public class WorkflowStateService {
    private static final AtomicLong idGenerator = new AtomicLong(0);
    private final List<WorkflowState> workflowStates = new ArrayList<>();

    @Nonnull
    public Optional<WorkflowState> findUserState(@Nonnull ChannelType channel, @Nonnull String userId) {
        return workflowStates.stream()
                .filter(getUserStatePredicateByChannelTypeAndUserId(channel, userId))
                .findFirst()
                .map(WorkflowState::copy);
    }

    @Nonnull
    public Optional<WorkflowState> createUserState(@Nonnull ChannelType channel, @Nonnull String userId) {
        Channel userChannel = Channel.builder()
                .channelType(channel)
                .userId(userId)
                .build();

        WorkflowState workflowState = WorkflowState.builder()
                .id(idGenerator.addAndGet(1))
                .status(Status.NEW)
                .userChannels(
                        Collections.singletonList(userChannel)
                ).build();

        workflowStates.add(workflowState);

        return Optional.of(workflowState)
                .map(WorkflowState::copy);
    }

    @Nonnull
    public Optional<WorkflowState> updateSelectedTopic(@Nonnull Long id, @Nonnull String topic) {
        WorkflowState workflowState = findUserState(id);

        if (Objects.nonNull(workflowState)) {
            workflowState.setSelectedTopic(topic);
        }

        return Optional.ofNullable(workflowState)
                .map(WorkflowState::copy);
    }

    private WorkflowState findUserState(Long id) {
        return workflowStates.stream()
                .filter(getUserStatePredicateById(id))
                .findFirst().orElse(null);
    }

    private Predicate<? super WorkflowState> getUserStatePredicateByChannelTypeAndUserId(ChannelType channel, String userId) {
        return us -> us.getChannels().stream().anyMatch(getUserChannelPredicateBuChannelTypeAndUserId(channel, userId));
    }

    private Predicate<? super WorkflowState> getUserStatePredicateById(Long id) {
        return us -> us.getId().equals(id);
    }

    private Predicate<? super Channel> getUserChannelPredicateBuChannelTypeAndUserId(ChannelType channel, String userId) {
        return uc -> uc.getUserId().equals(userId) && uc.getType().equals(channel);
    }
}
