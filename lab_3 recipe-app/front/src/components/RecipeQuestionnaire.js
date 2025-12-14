import React, { useState, useEffect } from 'react';
import { sessionService } from '../services/sessionService';
import { API_ENDPOINTS } from '../config/api';
import { getImageUrl } from '../components/imageUtils';
import RecipeResultModal from './RecipeResultModal';
import '../styles/RecipeQuestionnaire.css';

const RecipeQuestionnaire = ({ user, onClose, onRecipeFound }) => {
  const [currentQuestion, setCurrentQuestion] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [foundRecipe, setFoundRecipe] = useState(null);
  const [showRecipe, setShowRecipe] = useState(false);
  const [sessionHistory, setSessionHistory] = useState([]);
  const [error, setError] = useState(null);
  const [currentSessionId, setCurrentSessionId] = useState(null);
  const [useFrontendLogic, setUseFrontendLogic] = useState(false);

  useEffect(() => {
    startSession();
  }, []);

  const transformBackendQuestion = (backendQuestion) => {
    if (!backendQuestion) return null;

    console.log('🔄 Преобразование вопроса с бэкенда:', backendQuestion);

    return {
      id: backendQuestion.questionId,
      text: backendQuestion.questionText,
      type: backendQuestion.questionType,
      options: backendQuestion.answers ? backendQuestion.answers.map(answer => ({
        value: answer.answerId,
        text: answer.answerText,
        emoji: getEmojiForAnswer(answer.answerId, answer.answerText)
      })) : []
    };
  };

  const getEmojiForAnswer = (answerId, answerText) => {
    const emojiMap = {
      'yes': '✅',
      'no': '❌',
      'Да': '✅', 
      'Нет': '❌'
    };
    return emojiMap[answerId] || emojiMap[answerText] || '🔹';
  };

  const startSession = async () => {
    try {
      setLoading(true);
      setError(null);
      
      const userId = user?.userId || localStorage.getItem('userId');
      
      if (!userId) {
        throw new Error('User ID not found');
      }

      try {
        console.log('🔄 Пытаемся начать сессию через бэкенд...');
        const sessionResponse = await sessionService.startSession(userId);
        console.log('🔍 Полный ответ от бэкенда:', sessionResponse);
        
        if (sessionResponse && sessionResponse.sessionId && sessionResponse.currentQuestion) {
          const sessionId = sessionResponse.sessionId;
          setCurrentSessionId(sessionId);
          
          const transformedQuestion = transformBackendQuestion(sessionResponse.currentQuestion);
          
          if (transformedQuestion) {
            setCurrentQuestion(transformedQuestion);
            setQuestions([transformedQuestion]);
            setUseFrontendLogic(false);
            console.log('✅ Используем вопрос с бэкенда:', transformedQuestion);
            return;
          } else {
            console.warn('❌ Не удалось преобразовать вопрос с бэкенда');
          }
        } else {
          console.warn('❌ Неверный формат ответа от бэкенда:', sessionResponse);
          throw new Error('Invalid response from backend');
        }
        
      } catch (backendError) {
        console.log('🔄 Бэкенд недоступен, используем фронтенд логику:', backendError);
        setUseFrontendLogic(true);
        setError('Внимание: работаем в автономном режиме');
      }
      
    } catch (error) {
      console.error('❌ Ошибка начала сессии:', error);
      setError('Не удалось начать сессию. Пожалуйста, попробуйте позже.');
    } finally {
      setLoading(false);
    }
  };

  const handleAnswer = async (answer) => {
    try {
      setError(null);
      
      if (!currentQuestion) {
        throw new Error('Текущий вопрос не найден');
      }

      const userId = user?.userId || localStorage.getItem('userId');
      
      console.log('🔄 Обработка ответа:', {
        questionId: currentQuestion.id,
        answer: answer,
        useFrontendLogic: useFrontendLogic,
        sessionId: currentSessionId
      });

      let nextStep;
      let recipeResult = null;

      if (useFrontendLogic || !currentSessionId) {
        console.log('🔄 Используем фронтенд логику');
        setError('Фронтенд логика временно недоступна. Пожалуйста, используйте бэкенд.');
        return;
      } else {
        // Бэкенд логика
        try {
          console.log('🔄 Отправляем ответ на бэкенд...');
          
          const response = await sessionService.submitAnswer(
            userId,
            currentSessionId,
            currentQuestion.id,
            answer
          );

          console.log('🔍 Ответ от бэкенда:', response);

          // Бэкенд возвращает ответ в формате ApiResponse<Object>
          const responseData = response;
          
          // Проверяем тип ответа - рецепт или вопрос
          if (responseData.recipe) {
            // Найден рецепт
            recipeResult = responseData.recipe;
            console.log('🎉 Найден рецепт через бэкенд:', recipeResult);
          } else if (responseData.currentQuestion) {
            // Следующий вопрос
            const nextQuestion = transformBackendQuestion(responseData.currentQuestion);
            if (nextQuestion) {
              nextStep = nextQuestion;
              console.log('➡️ Следующий вопрос через бэкенд:', nextQuestion);
            } else {
              throw new Error('Не удалось преобразовать следующий вопрос');
            }
          } else if (responseData.sessionId && responseData.currentQuestion) {
            // Альтернативный формат - SessionResponse
            const nextQuestion = transformBackendQuestion(responseData.currentQuestion);
            if (nextQuestion) {
              nextStep = nextQuestion;
              console.log('➡️ Следующий вопрос через бэкенд (SessionResponse):', nextQuestion);
            }
          } else {
            console.warn('📦 Неизвестный формат данных от бэкенда:', responseData);
            throw new Error('Неизвестный формат ответа от сервера');
          }
          
        } catch (backendError) {
          console.log('🔄 Бэкенд недоступен, переключаемся на фронтенд логику:', backendError);
          setUseFrontendLogic(true);
          setError('Бэкенд недоступен. Пожалуйста, попробуйте позже.');
          return;
        }
      }

      // Сохраняем историю
      const selectedOption = currentQuestion.options.find(opt => opt.value === answer);
      setSessionHistory(prev => [...prev, {
        question: currentQuestion.text,
        answer: selectedOption ? selectedOption.text : answer,
        timestamp: new Date().toISOString()
      }]);

      // Обрабатываем результат
      if (recipeResult) {
        console.log('🎯 Обработка рецепта от бэкенда');
        const recipeWithImage = {
          ...recipeResult,
          imageUrl: getImageUrl(recipeResult.imageUrl)
        };
        setFoundRecipe(recipeWithImage);
        setShowRecipe(true);
      } else if (nextStep && typeof nextStep === 'object') {
        console.log('🎯 Переход к следующему вопросу от бэкенда');
        setCurrentQuestion(nextStep);
        if (!questions.find(q => q.id === nextStep.id)) {
          setQuestions(prev => [...prev, nextStep]);
        }
      } else {
        throw new Error('Неизвестный тип следующего шага');
      }
      
    } catch (error) {
      console.error('❌ Ошибка обработки ответа:', error);
      setError(`Ошибка: ${error.message}`);
    }
  };

  const handlePrevious = () => {
    if (sessionHistory.length > 0 && questions.length > 1) {
      const previousQuestions = questions.slice(0, -1);
      const prevQuestion = previousQuestions[previousQuestions.length - 1];
      const prevHistory = sessionHistory.slice(0, -1);
      
      if (prevQuestion) {
        console.log('↩️ Возврат к предыдущему вопросу:', prevQuestion.text);
        setCurrentQuestion(prevQuestion);
        setQuestions(previousQuestions);
        setSessionHistory(prevHistory);
        setError(null);
      }
    }
  };

  const handleAcceptRecipe = () => {
    if (foundRecipe && onRecipeFound) {
      console.log('✅ Принят рецепт:', foundRecipe.name);
      
      const recipeWithDbId = {
        ...foundRecipe,
        id: foundRecipe.id,
        questionHistory: [...sessionHistory],
        matchedAt: new Date().toISOString()
      };
      
      console.log('🔍 Рецепт для сохранения:', recipeWithDbId);
      onRecipeFound(recipeWithDbId);
    }
  };

  const handleRestart = () => {
    console.log('🔄 Перезапуск сессии');
    setCurrentQuestion(null);
    setQuestions([]);
    setFoundRecipe(null);
    setShowRecipe(false);
    setSessionHistory([]);
    setError(null);
    setCurrentSessionId(null);
    setUseFrontendLogic(false);
    startSession();
  };

  if (loading) {
    return (
      <div className="questionnaire-modal">
        <div className="modal-content">
          <div className="loading">
            <div className="loading-spinner"></div>
            <p>Загрузка вопросов...</p>
          </div>
        </div>
      </div>
    );
  }

  if (showRecipe && foundRecipe) {
    return (
      <RecipeResultModal
        recipe={foundRecipe}
        sessionHistory={sessionHistory}
        onClose={onClose}
        onRestart={handleRestart}
        onAcceptRecipe={handleAcceptRecipe}
      />
    );
  }

  if (!currentQuestion) {
    return (
      <div className="questionnaire-modal">
        <div className="modal-content">
          <div className="error-state">
            <h3>Не удалось начать опрос</h3>
            <p>Попробуйте перезагрузить страницу</p>
            <button className="btn-primary" onClick={startSession}>
              🔄 Попробовать снова
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="questionnaire-modal">
      <div className="modal-content">
        <div className="modal-header">
          <h2>Подбор рецепта</h2>
          <button className="close-btn" onClick={onClose}>×</button>
        </div>
        
        {error && (
          <div className={`error-message ${error.includes('Внимание') ? 'warning-message' : ''}`}>
            ⚠️ {error}
          </div>
        )}
        
        <div className="progress-section">
          <div className="progress-bar">
            <div 
              className="progress-fill"
              style={{ 
                width: `${((questions.findIndex(q => q.id === currentQuestion.id) + 1) / Math.max(questions.length, 5)) * 100}%` 
              }}
            ></div>
          </div>
          <div className="session-info">
            {useFrontendLogic ? 'Автономный режим' : 'Подключено к серверу'} • Вопрос {questions.findIndex(q => q.id === currentQuestion.id) + 1} из ~{Math.max(questions.length, 5)}
          </div>
        </div>

        <div className="question-section">
          <h3 className="question-text">{currentQuestion.text}</h3>
          
          <div className="options-grid">
            {currentQuestion.options.map((option, index) => (
              <button
                key={index}
                className="option-button"
                onClick={() => handleAnswer(option.value)}
                disabled={!!error && !error.includes('Внимание')}
              >
                {option.emoji && <span className="option-emoji">{option.emoji}</span>}
                <span className="option-text">{option.text}</span>
              </button>
            ))}
          </div>
        </div>

        <div className="navigation-actions">
          <button 
            className="nav-button prev-button"
            onClick={handlePrevious}
            disabled={sessionHistory.length === 0}
          >
            ← Назад
          </button>
          
          <button className="nav-button restart-button" onClick={handleRestart}>
            🔄 Заново
          </button>
        </div>
      </div>
    </div>
  );
};

export default RecipeQuestionnaire;