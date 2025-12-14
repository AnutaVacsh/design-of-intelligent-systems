package ru.lama.expertCookingSystem.drools.facts;

import lombok.Data;
import java.util.List;

@Data
public class QuestionFact {
  private String questionId;
  private String questionText;
  private String questionType; // "SINGLE_SELECT", "MULTI_SELECT", "TEXT"
  private List<AnswerOption> answers;

  @Data
  public static class AnswerOption {
    private String answerId;
    private String answerText;

    public AnswerOption() {}

    public AnswerOption(String answerId, String answerText) {
      this.answerId = answerId;
      this.answerText = answerText;
    }
  }

  public QuestionFact() {}

  public QuestionFact(String questionId, String questionText, String questionType, List<AnswerOption> answers) {
    this.questionId = questionId;
    this.questionText = questionText;
    this.questionType = questionType;
    this.answers = answers;
  }
}