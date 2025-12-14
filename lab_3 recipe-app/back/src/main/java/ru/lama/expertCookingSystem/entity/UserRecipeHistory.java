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
@Table(name = "user_recipe_history",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "recipe_id", "matched_at"}))
public class UserRecipeHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recipe_id", nullable = false)
  private Recipe recipe;

  @CreationTimestamp
  @Column(name = "matched_at", nullable = false, updatable = false)
  private LocalDateTime matchedAt;

  @Column
  private Integer rating;
}