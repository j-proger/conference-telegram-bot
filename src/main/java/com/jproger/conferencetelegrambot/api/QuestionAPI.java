package com.jproger.conferencetelegrambot.api;

import com.jproger.conferencetelegrambot.entities.Question;
import java.util.List;

public interface QuestionAPI {

  List<Question> getQuestions();

  void addQuestion(Question question);

}
