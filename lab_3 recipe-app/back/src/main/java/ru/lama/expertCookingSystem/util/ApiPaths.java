package ru.lama.expertCookingSystem.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPaths {
  public static final String API = "/api";

  // Auth
  public static final String AUTH = API + "/auth";
  public static final String SIGNUP = "/signup";
  public static final String LOGIN = "/login";

  // Sessions
  public static final String SESSIONS = API + "/sessions";
  public static final String SESSIONS_START = "/start";
  public static final String SESSIONS_ANSWER = "/answer";
  public static final String SESSIONS_CURRENT = "/current";
  public static final String SESSIONS_CANCEL = "/{sessionId}";

  // Users
  public static final String USERS = API + "/users";
  public static final String USERS_PROFILE = "/profile";
  public static final String USERS_PREFERENCES = "/preferences";
  public static final String USERS_HISTORY = "/history";

  // Recipes
  public static final String RECIPES = API + "/recipes";
  public static final String RECIPES_ID = "/{recipeId}";
  public static final String RECIPES_RATING = "/{recipeId}/rating";
  public static final String RECIPES_BATCH = "/batch";

  // Preference test paths
  public static final String PREFERENCE_TEST = "/api/preference-test";
  public static final String PREFERENCE_TEST_QUESTIONS = "/questions";
  public static final String PREFERENCE_TEST_SUBMIT = "/submit";
}
