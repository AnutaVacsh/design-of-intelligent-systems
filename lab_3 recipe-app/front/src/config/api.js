// config/api.js
export const API_BASE_URL = 'http://localhost:8080';

export const API_ENDPOINTS = {
  AUTH: {
    SIGNUP: `${API_BASE_URL}/api/auth/signup`,
    LOGIN: `${API_BASE_URL}/api/auth/login`,
    LOGOUT: `${API_BASE_URL}/api/auth/logout`
  },
  SESSION: {
    START: `${API_BASE_URL}/api/sessions/start`,
    ANSWER: `${API_BASE_URL}/api/sessions/answer`,
    CURRENT: `${API_BASE_URL}/api/sessions/current`,
    END: `${API_BASE_URL}/api/sessions/{sessionId}`
  },
  USERS: {
    PROFILE: `${API_BASE_URL}/api/users/profile`,
    PREFERENCES: `${API_BASE_URL}/api/users/preferences`,
    HISTORY: `${API_BASE_URL}/api/users/history`
  },
  RECIPES: {
    LIST: `${API_BASE_URL}/api/recipes/batch`,
    DETAIL: `${API_BASE_URL}/api/recipes`,
    GET_ONE: (id) => `${API_BASE_URL}/api/recipes/${id}`,
    RATING: (recipeId) => `${API_BASE_URL}/api/recipes/${recipeId}/rating`,
    BATCH: `${API_BASE_URL}/api/recipes/batch`
  },
  QUESTIONS: {
    LIST: `${API_BASE_URL}/api/questions`,
    ROOT: `${API_BASE_URL}/api/questions/root`,
    GET_ONE: (id) => `${API_BASE_URL}/api/questions/${id}`
  },
  PREFERENCE_TEST: {
    QUESTIONS: `${API_BASE_URL}/api/preference-test/questions`
  },
  UPLOADS: {
    IMAGES: `${API_BASE_URL}/uploads/images`
  }
};

export const DEFAULT_FETCH_OPTIONS = {
  headers: {
    'Content-Type': 'application/json',
  },
};