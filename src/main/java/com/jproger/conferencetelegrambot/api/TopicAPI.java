package com.jproger.conferencetelegrambot.api;

import com.jproger.conferencetelegrambot.entities.Topic;

import java.util.Set;

public interface TopicAPI {
    Set<Topic> getTopics();
}
