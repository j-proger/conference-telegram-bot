package com.jproger.conferencetelegrambot.topics.mappers;

import com.jproger.conferencetelegrambot.topics.dto.QuestionDto;
import com.jproger.conferencetelegrambot.topics.entities.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {
    public QuestionDto toQuestionDtoMap(Question question) {
        return QuestionDto.builder()
                .id(question.getId())
                .description(question.getDescription())
                .topicId(question.getTopic().getId())
                .userId(question.getUserId())
                .build();
    }
}
