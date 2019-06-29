package com.jproger.conferencetelegrambot.model;

import com.jproger.conferencetelegrambot.api.QuestionAPI;
import com.jproger.conferencetelegrambot.entities.Question;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionStore implements QuestionAPI {

  private static volatile QuestionStore instance = null;

  private List<Question> questions = null;

  private QuestionStore() {
    this.questions = new ArrayList<>();
  }

  public static QuestionStore getInstance() {
    if (instance == null) {

      synchronized(QuestionStore.class) {
        if (instance == null) {
          instance = new QuestionStore();
        }
      }
    }
    return instance;
  }

  @Override
  public List<Question> getQuestions() {
    return questions;
  }

  @Override
  public void addQuestion(Question question) {
    questions.add(question);
  }
}
