package ru.lama.expertCookingSystem.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lama.expertCookingSystem.entity.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

//  List<Recipe> findTopRatedRecipes(int limit);
}
