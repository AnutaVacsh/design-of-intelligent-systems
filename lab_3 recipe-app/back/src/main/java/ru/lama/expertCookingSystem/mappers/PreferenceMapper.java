package ru.lama.expertCookingSystem.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.lama.expertCookingSystem.dto.UserPreferencesDto;
import ru.lama.expertCookingSystem.entity.DietType;
import ru.lama.expertCookingSystem.entity.UserPreferences;

@Mapper(componentModel = "spring")
public interface PreferenceMapper {

  @Mapping(target = "dietType", source = "dietType.name")
  UserPreferencesDto toUserPreferencesDto(UserPreferences preferences);

  default List<String> toDietTypeNames(List<DietType> dietTypes) {
    return dietTypes.stream()
        .map(DietType::getName)
        .toList();
  }
}
