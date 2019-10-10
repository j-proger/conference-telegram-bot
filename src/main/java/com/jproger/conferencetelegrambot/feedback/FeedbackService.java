package com.jproger.conferencetelegrambot.feedback;

import com.jproger.conferencetelegrambot.common.dto.PageRequestDto;
import com.jproger.conferencetelegrambot.common.dto.PageResponseDto;
import com.jproger.conferencetelegrambot.feedback.dto.FeedbackDto;
import com.jproger.conferencetelegrambot.feedback.entities.DobbyFeedback;
import com.jproger.conferencetelegrambot.feedback.mappers.FeedbackMapper;
import com.jproger.conferencetelegrambot.feedback.repositories.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        DobbyFeedback feedback = feedbackRepository.save(
                DobbyFeedback.builder()
                        .comment(text)
                        .userId(userId)
                        .build()
        );

        return feedbackMapper.toFeedbackDto(feedback);
    }

    @Nonnull
    public PageResponseDto<FeedbackDto> getFeedback(PageRequestDto pageRequest) {
        Page<FeedbackDto> feedback = feedbackRepository.findAll(PageRequest.of(pageRequest.getPage(), pageRequest.getSize()))
                .map(feedbackMapper::toFeedbackDto);

        return PageResponseDto.of(feedback);
    }
}
