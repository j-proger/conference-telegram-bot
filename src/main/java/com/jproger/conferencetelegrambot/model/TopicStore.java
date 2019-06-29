package com.jproger.conferencetelegrambot.model;

import com.jproger.conferencetelegrambot.api.TopicAPI;
import com.jproger.conferencetelegrambot.entities.Topic;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TopicStore implements TopicAPI {

    private enum TopicDef {

        TELEGRAM_BOT_IN_8_HOURS("Телеграм-бот за 8 часов"),
        SPRING_BOOT_IN_PRODUCTION("Spring-Boot на проде");

        TopicDef(String name) {
            this.name = name;
        }

        private String name;

        public String getKey() {
            return this.name();
        }

        public String getName() {
            return name;
        }
    }

    @Override
    public Set<Topic> getTopics() {
        return Arrays.stream(TopicDef.values())
                .map(topicDef -> Topic.builder().key(topicDef.getKey()).name(topicDef.getName()).build())
                .collect(Collectors.toSet());
    }
}
