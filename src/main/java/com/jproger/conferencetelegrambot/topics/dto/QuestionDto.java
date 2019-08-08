package com.jproger.conferencetelegrambot.topics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionDto {
    private Long id;
    private Long topicId;
    private Long userId;
    private String description;
}