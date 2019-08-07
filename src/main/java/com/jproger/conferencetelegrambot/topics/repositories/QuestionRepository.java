package com.jproger.conferencetelegrambot.topics.repositories;

import com.jproger.conferencetelegrambot.topics.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
