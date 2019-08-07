package com.jproger.conferencetelegrambot.topics.mappers;

import com.jproger.conferencetelegrambot.topics.dto.TopicDto;
import com.jproger.conferencetelegrambot.topics.entities.Topic;
import org.springframework.stereotype.Component;

@Component
public class TopicMapper {
    public TopicDto toTopicDtoMap(Topic topic) {
        return TopicDto.builder()
                .id(topic.getId())
                .key(topic.getKey())
                .name(topic.getName())
                .build();
    }
}
