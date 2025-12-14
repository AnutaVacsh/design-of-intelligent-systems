package ru.lama.expertCookingSystem.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "ingredients")
@Entity
@Table(name = "ingredient_categories")
public class IngredientCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true, length = 100)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  // Relationships
  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private List<Ingredient> ingredients = new ArrayList<>();
}