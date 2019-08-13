package com.jproger.conferencetelegrambot.topics;

import com.jproger.conferencetelegrambot.common.dto.PageRequestDto;
import com.jproger.conferencetelegrambot.common.dto.PageResponseDto;
import com.jproger.conferencetelegrambot.topics.dto.QuestionDto;
import com.jproger.conferencetelegrambot.topics.dto.TopicDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/topics")
public class TopicController {
    private final TopicService topicService;

    @GetMapping
    public PageResponseDto<TopicDto> getTopics(PageRequestDto page) {
        return topicService.getTopics(page);
    }

    @GetMapping("/{topicId}/questions")
    public PageResponseDto<QuestionDto> getTopicQuestions(@PathVariable long topicId,
                                                          PageRequestDto page) {
        return topicService.getTopicQuestions(topicId, page);
    }
}
