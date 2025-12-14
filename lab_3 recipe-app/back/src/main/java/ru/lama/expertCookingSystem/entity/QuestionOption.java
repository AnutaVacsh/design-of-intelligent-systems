package ru.lama.expertCookingSystem.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "question_options")
public class QuestionOption {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id", nullable = false)
  private PreferenceQuestion question;

  @Column(name = "option_key", nullable = false)
  private String optionKey;

  @Column(name = "option_text", nullable = false)
  private String optionText;

  @Column(name = "emoji")
  private String emoji;

  @Column(name = "is_default")
  private Boolean isDefault = false;

  @Column(name = "sort_order")
  private Integer sortOrder = 0;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;
}