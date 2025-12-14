package ru.lama.expertCookingSystem.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "userPreferences")
@Entity
@Table(name = "diet_types")
public class DietType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true, length = 50)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  // Relationships
  @OneToMany(mappedBy = "dietType", fetch = FetchType.LAZY)
  private List<UserPreferences> userPreferences = new ArrayList<>();
}