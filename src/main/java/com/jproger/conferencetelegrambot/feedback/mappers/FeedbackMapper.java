package com.jproger.conferencetelegrambot.feedback.mappers;

import com.jproger.conferencetelegrambot.feedback.dto.FeedbackDto;
import com.jproger.conferencetelegrambot.feedback.entities.Feedback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Slf4j
@Component
public class FeedbackMapper {
    public FeedbackDto toFeedbackDto(@Nonnull Feedback feedback) {
        return FeedbackDto.builder()
                .id(feedback.getId())
                .comment(feedback.getComment())
                .userId(feedback.getUserId())
                .build();
    }
}
