package ru.lama.expertCookingSystem.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lama.expertCookingSystem.dto.request.LoginRequest;
import ru.lama.expertCookingSystem.dto.request.SignupRequest;
import ru.lama.expertCookingSystem.dto.response.AuthResponse;
import ru.lama.expertCookingSystem.entity.User;
import ru.lama.expertCookingSystem.entity.UserPreferences;
import ru.lama.expertCookingSystem.repositories.UserPreferencesRepository;
import ru.lama.expertCookingSystem.repositories.UserRepository;


/**
 * Сервис для обработки бизнес-логики аутентификации и регистрации
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

  private final UserRepository userRepository;
  private final UserPreferencesRepository userPreferencesRepository;

  /**
   * Регистрация нового пользователя в системе
   *
   * @param request данные для регистрации
   * @return данные зарегистрированного пользователя
   * @throws IllegalArgumentException если данные невалидны или пользователь уже существует
   */
  public AuthResponse registerUser(SignupRequest request) {
    log.info("Starting user registration for username: {}", request.username());

    validateSignupRequest(request);
    checkUserUniqueness(request);

    User newUser = createUser(request);
    createDefaultPreferences(newUser);

    log.info("User registered successfully with ID: {}", newUser.getId());
    return new AuthResponse(newUser.getId(), newUser.getUsername());
  }

  /**
   * Аутентификация пользователя в системе
   *
   * @param request данные для входа
   * @return данные аутентифицированного пользователя
   * @throws IllegalArgumentException если данные невалидны или аутентификация не удалась
   */
  public AuthResponse authenticateUser(LoginRequest request) {
    log.info("Attempting authentication for username: {}", request.username());

    validateLoginRequest(request);

    User user = findUserByUsername(request.username());
    verifyPassword(request.password(), user.getPasswordHash());

    log.info("User authenticated successfully: {}", request.username());
    return new AuthResponse(user.getId(), user.getUsername());
  }

  /**
   * Валидация данных регистрации
   */
  private void validateSignupRequest(SignupRequest request) {
    if (request.username() == null || request.username().trim().isEmpty()) {
      throw new IllegalArgumentException("Username cannot be empty");
    }
    if (request.email() == null || request.email().trim().isEmpty()) {
      throw new IllegalArgumentException("Email cannot be empty");
    }
    if (request.password() == null || request.password().length() < 6) {
      throw new IllegalArgumentException("Password must be at least 6 characters long");
    }
    if (request.username().length() < 3) {
      throw new IllegalArgumentException("Username must be at least 3 characters long");
    }
  }

  /**
   * Валидация данных входа
   */
  private void validateLoginRequest(LoginRequest request) {
    if (request.username() == null || request.username().trim().isEmpty()) {
      throw new IllegalArgumentException("Username cannot be empty");
    }
    if (request.password() == null || request.password().isEmpty()) {
      throw new IllegalArgumentException("Password cannot be empty");
    }
  }

  /**
   * Проверка уникальности username и email
   */
  private void checkUserUniqueness(SignupRequest request) {
    if (userRepository.existsByUsername(request.username())) {
      throw new IllegalArgumentException("Username already exists");
    }
    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("Email already registered");
    }
  }

  /**
   * Создание нового пользователя
   */
  private User createUser(SignupRequest request) {
    User user = new User();
    user.setUsername(request.username());
    user.setEmail(request.email());
    user.setPasswordHash(hashPassword(request.password()));
    return userRepository.save(user);
  }

  /**
   * Создание дефолтных предпочтений для пользователя
   */
  private void createDefaultPreferences(User user) {
    UserPreferences preferences = new UserPreferences();
    preferences.setUser(user);
    userPreferencesRepository.save(preferences);
  }

  /**
   * Поиск пользователя по username
   */
  private User findUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
  }

  /**
   * Проверка пароля
   */
  private void verifyPassword(String rawPassword, String hashedPassword) {
    if (!checkPassword(rawPassword, hashedPassword)) {
      throw new IllegalArgumentException("Invalid username or password");
    }
  }

  private String hashPassword(String password) {
    // TODO: Заменить на BCrypt
    return "hashed_" + password;
  }

  private boolean checkPassword(String rawPassword, String hashedPassword) {
    // TODO: Заменить на BCrypt
    return hashedPassword.equals("hashed_" + rawPassword);
  }
}