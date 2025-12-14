package ru.lama.expertCookingSystem.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"ingredients", "history"})
@Entity
@Table(name = "recipes")
public class Recipe {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 200)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String instructions;

  @Column(name = "prep_time")
  private Integer prepTime;

  @Column(name = "cook_time")
  private Integer cookTime;

  @Column(name = "difficulty_level", length = 20)
  private String difficultyLevel;

  @Column(name = "image_url", length = 500)
  private String imageUrl;

  @Column(nullable = false)
  private Integer servings = 1;

  @Column(name = "cuisine_type", length = 50)
  private String cuisineType;

  @Column(name = "is_vegetarian")
  private Boolean isVegetarian = false;

  @Column(name = "is_vegan")
  private Boolean isVegan = false;

  @Column(name = "is_gluten_free")
  private Boolean isGlutenFree = false;

  @Column(name = "is_dairy_free")
  private Boolean isDairyFree = false;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  // Relationships
  @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<RecipeIngredient> ingredients = new ArrayList<>();

  @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
  private List<UserRecipeHistory> history = new ArrayList<>();
}