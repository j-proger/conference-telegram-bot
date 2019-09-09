package com.jproger.conferencetelegrambot.feedback;

import com.jproger.conferencetelegrambot.common.dto.PageRequestDto;
import com.jproger.conferencetelegrambot.common.dto.PageResponseDto;
import com.jproger.conferencetelegrambot.feedback.dto.FeedbackDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping
    @ApiOperation("Получение списка отзывов о системе")
    public PageResponseDto<FeedbackDto> getFeedback(PageRequestDto pageRequest) {
        return feedbackService.getFeedback(pageRequest);
    }
}
