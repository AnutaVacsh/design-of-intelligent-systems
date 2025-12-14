package ru.lama.expertCookingSystem.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "recipe_ingredients",
    uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "ingredient_id"}))
public class RecipeIngredient {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recipe_id", nullable = false)
  private Recipe recipe;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ingredient_id", nullable = false)
  private Ingredient ingredient;

  @Column(length = 100)
  private String quantity;

  @Column(length = 50)
  private String unit;

  @Column(columnDefinition = "TEXT")
  private String notes;
}