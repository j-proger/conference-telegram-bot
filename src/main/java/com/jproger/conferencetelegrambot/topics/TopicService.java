package com.jproger.conferencetelegrambot.topics;

import com.jproger.conferencetelegrambot.topics.dto.QuestionDto;
import com.jproger.conferencetelegrambot.topics.entities.Question;
import com.jproger.conferencetelegrambot.topics.entities.Topic;
import com.jproger.conferencetelegrambot.topics.mappers.QuestionMapper;
import com.jproger.conferencetelegrambot.topics.mappers.TopicMapper;
import com.jproger.conferencetelegrambot.topics.repositories.QuestionRepository;
import com.jproger.conferencetelegrambot.topics.repositories.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public Optional<QuestionDto> createQuestion(Long userId, String topicKey, String description) {
        return topicRepository.findByKey(topicKey)
                .map(topic -> createQuestion(userId, topic, description))
                .map(questionMapper::toQuestionDtoMap);
    }

    private Question createQuestion(Long userId, Topic topic, String description) {
        return questionRepository.save(
                Question.builder()
                        .userId(userId)
                        .topic(topic)
                        .description(description)
                        .build()
        );
    }
}
