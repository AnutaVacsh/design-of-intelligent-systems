import React, { useState, useEffect } from 'react';
import { API_ENDPOINTS } from '../config/api';
import '../styles/PreferenceTest.css';

const PreferenceTest = ({ user, currentPreferences, onSave, onClose }) => {
  const [questions, setQuestions] = useState([]);
  const [answers, setAnswers] = useState({});
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadUserData();
  }, [currentPreferences]);

  const loadUserData = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // Загружаем вопросы с бэкенда
      await loadQuestions();
      
      // Инициализируем ответы из текущих предпочтений
      initializeAnswers();
      
    } catch (error) {
      console.log('Ошибка загрузки данных:', error);
      setError('Не удалось загрузить данные. Проверьте подключение к серверу.');
    } finally {
      setLoading(false);
    }
  };

  const loadQuestions = async () => {
    try {
      const response = await fetch(API_ENDPOINTS.PREFERENCE_TEST.QUESTIONS, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      
      if (response.ok) {
        const data = await response.json();
        if (data.success) {
          setQuestions(data.data);
        } else {
          throw new Error('Не удалось загрузить вопросы');
        }
      } else {
        throw new Error('Сервер не отвечает');
      }
    } catch (error) {
      console.error('Ошибка загрузки вопросов:', error);
      setError('Не удалось загрузить вопросы предпочтений.');
      setQuestions([]);
    }
  };

  const initializeAnswers = () => {
    if (currentPreferences) {
      // Используем переданные предпочтения
      initializeAnswersWithPreferences(currentPreferences);
    } else {
      // Или загружаем с сервера
      loadUserPreferences();
    }
  };

  const loadUserPreferences = async () => {
    try {
      const userId = user?.id || localStorage.getItem('userId');
      const token = localStorage.getItem('token');
      
      if (!userId) {
        initializeDefaultAnswers();
        return;
      }
      
      const response = await fetch(`${API_ENDPOINTS.USERS.PROFILE}?userId=${userId}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      
      if (response.ok) {
        const data = await response.json();
        if (data.success && data.data.preferences) {
          initializeAnswersWithPreferences(data.data.preferences);
        } else {
          initializeDefaultAnswers();
        }
      } else {
        initializeDefaultAnswers();
      }
      
    } catch (error) {
      console.log('Не удалось загрузить предпочтения:', error);
      initializeDefaultAnswers();
    }
  };

  const initializeAnswersWithPreferences = (preferences) => {
    const initialAnswers = {};
    
    // Тип питания - преобразуем в строчный ключ
    if (preferences.dietType) {
      // Если пришел ID (число), преобразуем в ключ
      if (typeof preferences.dietType === 'number') {
        initialAnswers.diet_type = convertIdToDietTypeKey(preferences.dietType);
      } else {
        // Если строка, приводим к нижнему регистру
        initialAnswers.diet_type = preferences.dietType.toLowerCase();
      }
    } else {
      initialAnswers.diet_type = 'no_preference'; // Значение по умолчанию
    }
    
    // Аллергии
    if (preferences.allergies && preferences.allergies.length > 0) {
      initialAnswers.allergies = [...preferences.allergies];
    } else {
      initialAnswers.allergies = ['NO_ALLERGIES'];
    }
    
    // Исключенные ингредиенты
    if (preferences.excludedIngredients && preferences.excludedIngredients.length > 0) {
      initialAnswers.excluded_ingredients = [...preferences.excludedIngredients];
    } else {
      initialAnswers.excluded_ingredients = ['NOTHING'];
    }
    
    console.log('Инициализированные ответы:', initialAnswers);
    setAnswers(initialAnswers);
  };

  const initializeDefaultAnswers = () => {
    const initialAnswers = {
      diet_type: 'no_preference', // Строчное значение
      allergies: ['NO_ALLERGIES'],
      excluded_ingredients: ['NOTHING']
    };
    
    setAnswers(initialAnswers);
  };

  // Преобразование ID в ключ
  const convertIdToDietTypeKey = (id) => {
    const idToKeyMap = {
      2: 'vegan',
      1: 'vegetarian',
      3: 'regular',
      4: 'no_preference'
    };
    return idToKeyMap[id] || 'no_preference';
  };

  // Преобразование ключа в ID
  const convertDietTypeKeyToId = (key) => {
    const keyToIdMap = {
      'vegan': 2,
      'vegetarian': 1,
      'regular': 3,
      'no_preference': 4
    };
    return keyToIdMap[key] || 4;
  };

  const handleAnswerChange = (questionId, value, questionType) => {
    console.log('Изменение ответа:', questionId, value, questionType);
    
    if (questionType === 'SINGLE') {
      setAnswers(prev => ({
        ...prev,
        [questionId]: value
      }));
    } else {
      setAnswers(prev => {
        const currentAnswers = prev[questionId] || [];
        let newAnswers;
        
        // Обработка специальных значений
        if (value === 'NO_ALLERGIES' || value === 'NOTHING') {
          newAnswers = [value];
        } else if (currentAnswers.includes('NO_ALLERGIES') || currentAnswers.includes('NOTHING')) {
          newAnswers = [value];
        } else {
          newAnswers = currentAnswers.includes(value)
            ? currentAnswers.filter(item => item !== value)
            : [...currentAnswers, value];
        }
        
        return {
          ...prev,
          [questionId]: newAnswers
        };
      });
    }
  };

  const savePreferencesToServer = async (preferencesData) => {
    try {
      const token = localStorage.getItem('token');
      const userId = user?.id || localStorage.getItem('userId');
      
      if (!userId) {
        throw new Error('User ID not found');
      }
      
      const url = `${API_ENDPOINTS.USERS.PREFERENCES}?userId=${userId}`;
      
      const response = await fetch(url, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(preferencesData)
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      return result;
    } catch (error) {
      console.error('Ошибка при сохранении на сервер:', error);
      throw error;
    }
  };

  const handleSubmit = async () => {
    try {
      setSaving(true);
      setError(null);
      
      // Преобразуем ключ типа питания в ID
      const dietTypeId = convertDietTypeKeyToId(answers.diet_type);
      
      // Формируем данные для отправки
      const preferencesData = {
        dietTypeId: dietTypeId,
        allergies: answers.allergies || [],
        excludedIngredients: answers.excluded_ingredients || []
      };
      
      console.log('Отправляем предпочтения:', preferencesData);

      // Сохраняем на сервере
      const result = await savePreferencesToServer(preferencesData);
      console.log('Предпочтения сохранены на сервере:', result);

      setError('✅ Предпочтения успешно сохранены!');

      // Вызываем колбэк сохранения
      if (onSave) {
        await onSave(preferencesData);
      }
      
      // Автоматически закрываем через 2 секунды
      setTimeout(() => {
        if (onClose) {
          onClose();
        }
      }, 2000);
      
    } catch (error) {
      console.error('Ошибка сохранения предпочтений:', error);
      setError(`❌ Ошибка при сохранении предпочтений: ${error.message}`);
    } finally {
      setSaving(false);
    }
  };

  const isFormValid = () => {
    return answers.diet_type && 
           answers.allergies && answers.allergies.length > 0 &&
           answers.excluded_ingredients && answers.excluded_ingredients.length > 0;
  };

  if (loading) {
    return (
      <div className="preference-test-modal">
        <div className="modal-content">
          <div className="loading-test">
            Загрузка ваших предпочтений...
          </div>
        </div>
      </div>
    );
  }

  if (questions.length === 0 && error) {
    return (
      <div className="preference-test-modal">
        <div className="modal-content">
          <div className="error-state">
            <h3>Не удалось загрузить вопросы</h3>
            <p>{error}</p>
            <button className="btn-secondary" onClick={onClose}>
              Закрыть
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="preference-test-modal">
      <div className="modal-content">
        <div className="modal-header">
          <h2>Настройка предпочтений</h2>
          <button className="close-btn" onClick={onClose} disabled={saving}>×</button>
        </div>
        
        <div className="test-description">
          <p>Ответьте на вопросы, чтобы мы могли подбирать рецепты именно для вас</p>
        </div>

        {error && (
          <div className={`error-message ${error.includes('✅') ? 'success-message' : ''}`}>
            {error}
            <button 
              onClick={() => setError(null)}
              className="close-error"
            >
              ×
            </button>
          </div>
        )}
        
        <div className="questions-container">
          {questions.map(question => (
            <div key={question.questionId} className="question-card" data-type={question.questionType}>
              <h3 className="question-title">
                <span className="question-number">Вопрос {question.order}</span>
                {question.questionText}
              </h3>
              
              <div className="options-grid">
                {question.options.map(option => (
                  <label 
                    key={option.optionKey} 
                    className={`option-label ${
                      (question.questionType === 'SINGLE' && answers[question.questionId] === option.optionKey) ||
                      (question.questionType === 'MULTIPLE' && answers[question.questionId]?.includes(option.optionKey))
                        ? 'selected' : ''
                    }`}
                  >
                    <input
                      type={question.questionType === 'SINGLE' ? 'radio' : 'checkbox'}
                      name={question.questionId}
                      value={option.optionKey}
                      checked={
                        question.questionType === 'SINGLE'
                          ? answers[question.questionId] === option.optionKey
                          : answers[question.questionId]?.includes(option.optionKey)
                      }
                      onChange={(e) => handleAnswerChange(
                        question.questionId, 
                        option.optionKey, 
                        question.questionType
                      )}
                      className="option-input"
                      disabled={saving}
                    />
                    
                    <span className="option-content">
                      <span className="option-emoji">{option.emoji}</span>
                      <span className="option-main">
                        <span className="option-text">{option.optionText}</span>
                        {option.description && (
                          <span className="option-description">{option.description}</span>
                        )}
                      </span>
                    </span>
                  </label>
                ))}
              </div>
            </div>
          ))}
        </div>
        
        <div className="modal-actions">
          <button className="btn-secondary" onClick={onClose} disabled={saving}>
            Отмена
          </button>
          <button 
            className="btn-primary" 
            onClick={handleSubmit}
            disabled={saving || !isFormValid()}
          >
            {saving ? 'Сохранение...' : 'Сохранить предпочтения'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default PreferenceTest;