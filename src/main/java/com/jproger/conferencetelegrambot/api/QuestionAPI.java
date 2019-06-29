package com.jproger.conferencetelegrambot.api;

import com.jproger.conferencetelegrambot.entities.Question;
import java.util.List;
import java.util.Set;

public interface QuestionAPI {

  Set<Question> getQuestions();

  void addQuestion(Question question);
}
