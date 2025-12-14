package ru.lama.expertCookingSystem.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.lama.expertCookingSystem.dto.response.AuthResponse;
import ru.lama.expertCookingSystem.entity.User;

@Mapper(componentModel = "spring")
public interface AuthMapper {

  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "username", source = "user.username")
  AuthResponse toAuthResponse(String token, User user);
}
