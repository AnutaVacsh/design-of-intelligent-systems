// decisionTreeService.js
import { decisionTree } from '../components/mockData.js';

export const getNextStep = (currentQuestion, answer) => {
  const questionNode = decisionTree[currentQuestion];
  if (!questionNode) return null;
  
  return questionNode[answer];
};

export const isRecipe = (step) => {
  return !decisionTree[step];
};

export const getInitialQuestion = () => {
  return "У вас есть желание попробовать экзотический рецепт?";
};

export const validateDecisionPath = (question, answer) => {
  const nextStep = getNextStep(question, answer);
  if (!nextStep) {
    console.error(`Invalid path: ${question} -> ${answer}`);
    return false;
  }
  return true;
};

// Новая функция для получения вопроса по тексту
export const getQuestionByText = (questionText, questions) => {
  return questions.find(q => q.text === questionText);
};

// Функция для получения вопроса по ID
export const getQuestionById = (questionId, questions) => {
  return questions.find(q => q.id === questionId);
};