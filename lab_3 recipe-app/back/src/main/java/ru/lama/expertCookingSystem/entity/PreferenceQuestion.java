package ru.lama.expertCookingSystem.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "preference_questions")
public class PreferenceQuestion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "field_name", nullable = false, unique = true)
  private String fieldName; // diet_type, allergies, excluded_ingredients

  @Column(name = "question_text", nullable = false)
  private String questionText;

  @Column(name = "question_type", nullable = false)
  private String questionType; // SINGLE, MULTIPLE

  @Column(name = "sort_order")
  private Integer sortOrder = 0;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @OrderBy("sortOrder ASC")
  private List<QuestionOption> options;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}