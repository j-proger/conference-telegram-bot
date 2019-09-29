package com.jproger.conferencetelegrambot.workflow.repositories;

import com.jproger.conferencetelegrambot.common.actions.Action.ChannelType;
import com.jproger.conferencetelegrambot.workflow.entities.UserState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStateRepository extends JpaRepository<UserState, Long> {
    Optional<UserState> findById(long id);

    Optional<UserState> findByChannelAndChannelUserId(ChannelType channel, String channelUserId);

    Optional<UserState> findByInnerUserId(long innerUserId);

    List<UserState> findByTopicId(long topicId);
}
