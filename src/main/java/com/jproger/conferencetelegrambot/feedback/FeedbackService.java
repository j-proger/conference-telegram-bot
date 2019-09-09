package com.jproger.conferencetelegrambot.feedback;

import com.jproger.conferencetelegrambot.feedback.dto.FeedbackDto;
import com.jproger.conferencetelegrambot.feedback.entities.Feedback;
import com.jproger.conferencetelegrambot.feedback.mappers.FeedbackMapper;
import com.jproger.conferencetelegrambot.feedback.repositories.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    @Nonnull
    public FeedbackDto createFeedback(long userId, @Nonnull String text) {
        Feedback feedback = feedbackRepository.save(
                Feedback.builder()
                        .comment(text)
                        .userId(userId)
                        .build()
        );

        return feedbackMapper.toFeedbackDto(feedback);
    }
}
