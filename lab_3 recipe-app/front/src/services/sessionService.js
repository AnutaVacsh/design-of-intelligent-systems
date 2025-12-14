// services/sessionService.js
import { apiService } from './apiService.js';
import { API_ENDPOINTS } from '../config/api';


export const sessionService = {
  // Начать новую сессию
  async startSession(userId) {
    try {
      const response = await apiService.startSession({
        userId: userId,
        startedAt: new Date().toISOString()
      });
      
      return response.data || response;
    } catch (error) {
      console.error('Error starting session:', error);
      throw error;
    }
  },

  // Отправить ответ на вопрос
  async submitAnswer(userId, sessionId, questionId, answer) {
    try {
      const answerData = {
        sessionId: sessionId,
        questionId: questionId,
        answers: [answer]
      };

      const response = await apiService.submitAnswer(userId, answerData);
      
      if (response.success && response.data) {
        return response.data;
      } else {
        throw new Error('Invalid response format from server');
      }
    } catch (error) {
      console.error('Error submitting answer:', error);
      throw error;
    }
  },

  // Преобразовать рецепт из формата бэкенда в формат фронтенда
  transformRecipeFromBackend(backendRecipe) {
    if (!backendRecipe) return null;
    
    return {
      id: backendRecipe.id,
      name: backendRecipe.name,
      description: backendRecipe.description,
      prepTime: backendRecipe.prepTime,
      cookTime: backendRecipe.cookTime,
      difficultyLevel: backendRecipe.difficultyLevel,
      servings: backendRecipe.servings,
      cuisineType: backendRecipe.cuisineType,
      vegetarian: backendRecipe.vegetarian,
      vegan: backendRecipe.vegan,
      glutenFree: backendRecipe.glutenFree,
      dairyFree: backendRecipe.dairyFree,
      ingredients: backendRecipe.ingredients ? backendRecipe.ingredients.map(ing => ({
        name: ing.name,
        amount: `${ing.quantity} ${ing.unit}`.trim()
      })) : [],
      instructions: backendRecipe.instructions ? 
        backendRecipe.instructions.split('\n').filter(step => step.trim()) : [],
      imageUrl: backendRecipe.imageUrl ? 
        `${API_ENDPOINTS.UPLOADS.IMAGES}/${backendRecipe.imageUrl}` : ''
    };
  }
};