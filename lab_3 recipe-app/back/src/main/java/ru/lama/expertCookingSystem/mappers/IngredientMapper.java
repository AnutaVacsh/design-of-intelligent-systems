package ru.lama.expertCookingSystem.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.lama.expertCookingSystem.dto.IngredientDto;
import ru.lama.expertCookingSystem.entity.Ingredient;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

//  @Mapping(target = "category", source = "category.name")
  IngredientDto toIngredientDto(Ingredient ingredient);

  List<IngredientDto> toIngredientDtoList(List<Ingredient> ingredients);
}
