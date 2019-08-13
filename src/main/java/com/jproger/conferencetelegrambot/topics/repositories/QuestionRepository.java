package com.jproger.conferencetelegrambot.topics.repositories;

import com.jproger.conferencetelegrambot.topics.entities.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllByTopicId(long topicId, Pageable page);
}
