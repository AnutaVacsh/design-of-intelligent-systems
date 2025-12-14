// services/ApiService.js
import { API_ENDPOINTS, DEFAULT_FETCH_OPTIONS } from '../config/api';

class ApiService {
  constructor() {
    this.baseOptions = DEFAULT_FETCH_OPTIONS;
  }

  async request(endpoint, options = {}) {
    try {
      const token = localStorage.getItem('token');
      const headers = {
        ...this.baseOptions.headers,
        ...options.headers,
      };

      if (token) {
        headers['Authorization'] = `Bearer ${token}`;
      }

      const response = await fetch(endpoint, {
        ...this.baseOptions,
        ...options,
        headers,
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      return await response.json();
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  // Auth methods
  async signup(signupData) {
    return this.request(API_ENDPOINTS.AUTH.SIGNUP, {
      method: 'POST',
      body: JSON.stringify(signupData),
    });
  }

  async login(loginData) {
    return this.request(API_ENDPOINTS.AUTH.LOGIN, {
      method: 'POST',
      body: JSON.stringify(loginData),
    });
  }

  // Session methods
  async startSession(sessionData) {
    return this.request(API_ENDPOINTS.SESSION.START, {
      method: 'POST',
      body: JSON.stringify(sessionData),
    });
  }

  async submitAnswer(userId, answerData) {
    return this.request(`${API_ENDPOINTS.SESSION.ANSWER}?userId=${userId}`, {
      method: 'POST',
      body: JSON.stringify(answerData),
    });
  }

  // Questions methods
  async getAllQuestions() {
    return this.request(API_ENDPOINTS.QUESTIONS.LIST);
  }

  async getRootQuestion() {
    return this.request(API_ENDPOINTS.QUESTIONS.ROOT);
  }

  async getQuestionById(id) {
    return this.request(API_ENDPOINTS.QUESTIONS.GET_ONE(id));
  }

  // Recipes methods
  async getRecipes() {
    return this.request(API_ENDPOINTS.RECIPES.LIST);
  }

  async getRecipeHistory() {
    return this.request(API_ENDPOINTS.USERS.HISTORY);
  }

  async getRecipeById(id) {
    return this.request(API_ENDPOINTS.RECIPES.GET_ONE(id));
  }

  async rateRecipe(recipeId, ratingData) {
    return this.request(API_ENDPOINTS.RECIPES.RATING(recipeId), {
      method: 'POST',
      body: JSON.stringify(ratingData),
    });
  }
}

export const apiService = new ApiService();