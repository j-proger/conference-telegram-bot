package com.jproger.conferencetelegrambot.topics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingDto {
    private Long id;

    private Long topicId;

    private Long userId;

    private int value;
}
