package ru.lama.expertCookingSystem.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.lama.expertCookingSystem.dto.UserPreferencesDto;
import ru.lama.expertCookingSystem.dto.request.UpdatePreferencesRequest;
import ru.lama.expertCookingSystem.dto.response.UserProfileResponse;
import ru.lama.expertCookingSystem.entity.User;
import ru.lama.expertCookingSystem.entity.UserPreferences;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "preferences", source = "preferences")
  UserProfileResponse toUserProfileResponse(User user);

  @Mapping(target = "dietType", source = "dietType.name")
  UserPreferencesDto toUserPreferencesDto(UserPreferences preferences);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "dietType", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void updatePreferencesFromDto(@MappingTarget UserPreferences preferences, UpdatePreferencesRequest dto);
}
