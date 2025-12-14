package ru.lama.expertCookingSystem.drools.facts;

import lombok.Data;
import java.util.List;

@Data
public class AnswerFact {
  private String sessionId;
  private String questionId;
  private List<String> selectedAnswers;

  public AnswerFact() {}

  public AnswerFact(String sessionId, String questionId, List<String> selectedAnswers) {
    this.sessionId = sessionId;
    this.questionId = questionId;
    this.selectedAnswers = selectedAnswers;
  }
}