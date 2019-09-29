package com.jproger.conferencetelegrambot.topics;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.actions.Action.ChannelType;
import com.jproger.conferencetelegrambot.common.actions.FinishTopicSystemAction;
import com.jproger.conferencetelegrambot.common.dto.PageRequestDto;
import com.jproger.conferencetelegrambot.common.dto.PageResponseDto;
import com.jproger.conferencetelegrambot.topics.dto.QuestionDto;
import com.jproger.conferencetelegrambot.topics.dto.TopicDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/topics")
public class TopicController {
    private final TopicService topicService;
    private final ActionBus actionBus;

    @GetMapping
    @ApiOperation("Получить список докладов. Постранично.")
    public PageResponseDto<TopicDto> getTopics(PageRequestDto page) {
        return topicService.getTopics(page);
    }

    @GetMapping("/{topicId}/questions")
    @ApiOperation("Получить список вопросов для доклада. Постранично.")
    public PageResponseDto<QuestionDto> getTopicQuestions(@PathVariable long topicId,
                                                          PageRequestDto page) {
        return topicService.getTopicQuestions(topicId, page);
    }

    @PutMapping("/{topicId}/finish")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation("Завершить доклад. Это запустит процесс сбора отзывов у пользователей")
    public void finishTopic(@PathVariable long topicId) {
        FinishTopicSystemAction action = new FinishTopicSystemAction(ChannelType.REST, null, topicId);

        actionBus.sendAction(action);
    }
}
