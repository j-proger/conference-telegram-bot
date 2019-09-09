package com.jproger.conferencetelegrambot.topics.repositories;

import com.jproger.conferencetelegrambot.topics.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByKey(String key);

    Optional<Topic> findByIdAndStatus(long id, Topic.Status status);
}
