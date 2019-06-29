package com.jproger.conferencetelegrambot.web;

import com.jproger.conferencetelegrambot.api.TopicAPI;
import com.jproger.conferencetelegrambot.model.TopicStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    public TopicAPI topicApi() {
        return new TopicStore();
    }
}
