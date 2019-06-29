package com.jproger.conferencetelegrambot.model;

import com.jproger.conferencetelegrambot.api.QuestionAPI;
import com.jproger.conferencetelegrambot.entities.Question;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class QuestionStore implements QuestionAPI {

  private Set<Question> questions = Collections.synchronizedSet(new HashSet<>());

  @Override
  public Set<Question> getQuestions() {
    return questions;
  }

  @Override
  public void addQuestion(Question question) {
    questions.add(question);
  }
}
