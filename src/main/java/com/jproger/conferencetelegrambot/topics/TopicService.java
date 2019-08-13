package com.jproger.conferencetelegrambot.topics;

import com.jproger.conferencetelegrambot.common.dto.PageRequestDto;
import com.jproger.conferencetelegrambot.common.dto.PageResponseDto;
import com.jproger.conferencetelegrambot.topics.dto.QuestionDto;
import com.jproger.conferencetelegrambot.topics.dto.TopicDto;
import com.jproger.conferencetelegrambot.topics.entities.Question;
import com.jproger.conferencetelegrambot.topics.entities.Topic;
import com.jproger.conferencetelegrambot.topics.mappers.QuestionMapper;
import com.jproger.conferencetelegrambot.topics.mappers.TopicMapper;
import com.jproger.conferencetelegrambot.topics.repositories.QuestionRepository;
import com.jproger.conferencetelegrambot.topics.repositories.TopicRepository;
import com.jproger.conferencetelegrambot.users.UserService;
import com.jproger.conferencetelegrambot.users.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class TopicService {
    private final UserService userService;
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public PageResponseDto<TopicDto> getTopics(@Valid PageRequestDto page) {
        Page<TopicDto> topics = topicRepository.findAll(PageRequest.of(page.getPage(), page.getSize()))
                .map(topicMapper::toTopicDtoMap);

        return PageResponseDto.of(topics);
    }

    public PageResponseDto<QuestionDto> getTopicQuestions(long topicId, @Valid PageRequestDto page) {
        Page<QuestionDto> questions = questionRepository.findAllByTopicId(topicId, PageRequest.of(page.getPage(), page.getSize()))
                .map(q -> questionMapper.toQuestionDtoMap(q, getUserById(q.getUserId())));

        return PageResponseDto.of(questions);
    }

    public Optional<TopicDto> getTopicByKey(String topicKey) {
        return topicRepository.findByKey(topicKey)
                .map(topicMapper::toTopicDtoMap);
    }

    public Optional<QuestionDto> createQuestion(Long userId, String topicKey, String description) {
        return topicRepository.findByKey(topicKey)
                .map(t -> createQuestion(userId, t, description))
                .map(q -> questionMapper.toQuestionDtoMap(q, getUserById(q.getUserId())));
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

    private UserDto getUserById(long userId) {
        return userService.getUserById(userId)
                .orElse(null);
    }
}
