package com.jproger.conferencetelegrambot.topics.mappers;

import com.jproger.conferencetelegrambot.topics.dto.RatingDto;
import com.jproger.conferencetelegrambot.topics.entities.Rating;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {
    public RatingDto toRatingDto(Rating rating) {
        return RatingDto.builder()
                .id(rating.getId())
                .topicId(rating.getTopic().getId())
                .userId(rating.getUserId())
                .value(rating.getValue())
                .build();
    }
}
