import React, { useState, useEffect } from 'react';
import { API_ENDPOINTS } from '../config/api';
import Header from '../components/Header';
import PreferenceTest from '../components/PreferenceTest';
import RecipeHistory from '../components/RecipeHistory';
import RecipeQuestionnaire from '../components/RecipeQuestionnaire';
import '../styles/Main.css';

const Main = ({ user, onLogout }) => {
  const [userProfile, setUserProfile] = useState(null);
  const [recipeHistory, setRecipeHistory] = useState([]);
  const [recipes, setRecipes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showPreferenceTest, setShowPreferenceTest] = useState(false);
  const [showQuestionnaire, setShowQuestionnaire] = useState(false);
  const [greeting, setGreeting] = useState('');
  const [preferenceOptions, setPreferenceOptions] = useState(null);

  // Восстанавливаем данные из localStorage при загрузке
  useEffect(() => {
    const savedProfile = localStorage.getItem('userProfile');
    const savedHistory = localStorage.getItem('recipeHistory');
    const savedRecipes = localStorage.getItem('recipes');
    
    if (savedProfile) {
      try {
        setUserProfile(JSON.parse(savedProfile));
      } catch (error) {
        console.error('Ошибка восстановления профиля:', error);
      }
    }
    
    if (savedHistory) {
      try {
        setRecipeHistory(JSON.parse(savedHistory));
      } catch (error) {
        console.error('Ошибка восстановления истории:', error);
      }
    }
    
    if (savedRecipes) {
      try {
        setRecipes(JSON.parse(savedRecipes));
      } catch (error) {
        console.error('Ошибка восстановления рецептов:', error);
      }
    }
    
    loadUserData();
    loadPreferenceOptions();
  }, []);

  // Загружаем опции предпочтений с бэкенда
  const loadPreferenceOptions = async () => {
    try {
      console.log('🔄 Загружаем опции предпочтений с бэкенда...');
      const response = await fetch(API_ENDPOINTS.PREFERENCE_TEST.QUESTIONS, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      
      if (response.ok) {
        const data = await response.json();
        if (data.success) {
          setPreferenceOptions(data.data);
          console.log('✅ Опции предпочтений загружены:', data.data);
        }
      }
    } catch (error) {
      console.error('❌ Ошибка загрузки опций предпочтений:', error);
    }
  };

  // Функция для получения названия и смайлика по ID из загруженных опций
  const getPreferenceDisplay = (id, questionType) => {
    if (!preferenceOptions || !id) return { name: id, emoji: '' };
    
    // Ищем вопрос с нужным типом
    const question = preferenceOptions.find(q => {
      if (questionType === 'dietType') return q.questionId === 'diet_type';
      if (questionType === 'allergy') return q.questionId === 'allergies';
      if (questionType === 'excludedIngredient') return q.questionId === 'excluded_ingredients';
      return false;
    });
    
    if (!question || !question.options) return { name: id, emoji: '' };
    
    // Ищем нужную опцию
    const option = question.options.find(opt => opt.optionKey === id);
    if (option) {
      return {
        name: option.optionText,
        emoji: option.emoji || ''
      };
    }
    
    // Специальные случаи
    if (id === 'NO_PREFERENCE') return { name: 'Без предпочтений', emoji: '🤷' };
    if (id === 'VEGETARIAN') return { name: 'Вегетарианское', emoji: '🥗' };
    if (id === 'VEGAN') return { name: 'Веганское', emoji: '🌱' };
    if (id === 'REGULAR') return { name: 'Обычное (без ограничений)', emoji: '🍖' };
    if (id === 'NO_ALLERGIES') return { name: 'Нет аллергий', emoji: '❌' };
    if (id === 'NOTHING') return { name: 'Ничего не исключаю', emoji: '❌' };
    
    return { name: id, emoji: '' };
  };

  // Сохраняем данные в localStorage при изменении
  useEffect(() => {
    if (userProfile) {
      localStorage.setItem('userProfile', JSON.stringify(userProfile));
    }
  }, [userProfile]);

  useEffect(() => {
    if (recipeHistory.length > 0) {
      localStorage.setItem('recipeHistory', JSON.stringify(recipeHistory));
    }
  }, [recipeHistory]);

  useEffect(() => {
    if (recipes.length > 0) {
      localStorage.setItem('recipes', JSON.stringify(recipes));
    }
  }, [recipes]);

  // Проверяем токен при загрузке
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      onLogout();
      return;
    }
  }, [onLogout]);
  
  // Определение приветствия по времени суток
useEffect(() => {
  const hour = new Date().getHours();
  
  if (hour >= 23 || hour < 5) {
    setGreeting('Доброй ночи');
  } else if (hour < 12) {
    setGreeting('Доброе утро');
  } else if (hour < 18) {
    setGreeting('Добрый день');
  } else {
    setGreeting('Добрый вечер');
  }
}, []);

  // Функция для загрузки деталей рецептов
  const loadRecipeDetails = async (historyItems) => {
    try {
      console.log('Начинаем загрузку деталей рецептов...');
      
      const recipesWithDetails = [];
      const recipeIds = historyItems.map(item => item.recipeId).filter(Boolean);
      
      console.log('ID рецептов для загрузки (в порядке истории):', recipeIds);
      
      if (recipeIds.length > 0) {
        let recipesData = null;
        
        try {
          console.log('Пробуем эндпоинт /api/recipes/batch...');
          const recipesResponse = await fetch(API_ENDPOINTS.RECIPES.BATCH, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify({ recipeIds })
          });
          
          if (recipesResponse.ok) {
            recipesData = await recipesResponse.json();
            console.log('Batch данные:', recipesData);
          }
        } catch (batchError) {
          console.log('Batch не сработал:', batchError);
        }
        
        if (!recipesData || !recipesData.success) {
          console.log('Загружаем рецепты по одному в порядке истории...');
          
          for (const historyItem of historyItems) {
            try {
              const recipeId = historyItem.recipeId;
              if (recipeId) {
                console.log(`Загружаем рецепт ${recipeId}...`);
                
                let recipeResponse = await fetch(API_ENDPOINTS.RECIPES.GET_ONE(recipeId), {
                  headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                  }
                });
                
                if (!recipeResponse.ok) {
                  recipeResponse = await fetch(`${API_ENDPOINTS.RECIPES.DETAIL}/${recipeId}`, {
                    headers: {
                      'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                  });
                }
                
                if (recipeResponse && recipeResponse.ok) {
                  const recipeData = await recipeResponse.json();
                  console.log(`Данные рецепта ${recipeId}:`, recipeData);
                  
                  if (recipeData.success) {
                    recipesWithDetails.push({
                      ...recipeData.data,
                      ...historyItem,
                      historyId: historyItem.recipeId || historyItem.id,
                      // Сохраняем оригинальную дату для сортировки
                      originalMatchedAt: historyItem.matchedAt || historyItem.createdAt
                    });
                  }
                } else {
                  console.log(`Рецепт ${recipeId} не найден, используем данные истории`);
                  recipesWithDetails.push({
                    id: historyItem.recipeId,
                    name: historyItem.name || 'Рецепт',
                    imageUrl: historyItem.imageUrl,
                    description: historyItem.description || '',
                    historyId: historyItem.recipeId,
                    matchedAt: historyItem.matchedAt,
                    originalMatchedAt: historyItem.matchedAt || historyItem.createdAt,
                    rating: historyItem.rating,
                    vegetarian: false,
                    vegan: false,
                    glutenFree: false,
                    dairyFree: false,
                    ingredients: []
                  });
                }
              }
            } catch (error) {
              console.error(`Ошибка загрузки рецепта ${historyItem.recipeId}:`, error);
            }
          }
        } else if (recipesData.success) {
          // Сохраняем порядок из historyItems
          historyItems.forEach(historyItem => {
            const recipe = recipesData.data.find(r => r.id === historyItem.recipeId);
            if (recipe) {
              recipesWithDetails.push({
                ...recipe,
                ...historyItem,
                historyId: historyItem.recipeId,
                // Сохраняем оригинальную дату для сортировки
                originalMatchedAt: historyItem.matchedAt || historyItem.createdAt
              });
            }
          });
        }
      }
      
      // Дополнительная сортировка на всякий случай
      const sortedRecipes = recipesWithDetails.sort((a, b) => {
        const dateA = new Date(a.originalMatchedAt || a.matchedAt || 0);
        const dateB = new Date(b.originalMatchedAt || b.matchedAt || 0);
        return dateB - dateA; // Сначала новые
      });
      
      console.log('🎯 Итоговые рецепты с деталями (отсортированные):', sortedRecipes.map(r => ({
        name: r.name,
        date: r.originalMatchedAt || r.matchedAt,
        id: r.id
      })));
      
      setRecipes(sortedRecipes);
      
    } catch (error) {
      console.error('Ошибка загрузки деталей рецептов:', error);
      setRecipes([]);
    }
  };

  const loadUserData = async () => {
    try {
      setLoading(true);
      
      // Загружаем реальные данные
      await loadRealData();
      
    } catch (error) {
      console.error('Ошибка загрузки данных:', error);
      // Если есть сохраненные данные, используем их
      if (!userProfile) {
        setUserProfile({
          username: user?.username,
          email: user?.email,
          preferences: null
        });
      }
    } finally {
      setLoading(false);
    }
  };

  const loadRealData = async () => {
    try {
      // Загрузка профиля
      const profileResponse = await fetch(`${API_ENDPOINTS.USERS.PROFILE}?userId=${user?.userId}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      
      if (profileResponse.ok) {
        const profileData = await profileResponse.json();
        console.log('Профиль данные:', profileData);
        
        if (profileData.success) {
          setUserProfile(profileData.data);
        } else {
          setUserProfile({
            username: user?.username,
            email: user?.email,
            preferences: null
          });
        }
      }

      // Загрузка истории
      const historyResponse = await fetch(`${API_ENDPOINTS.USERS.HISTORY}?userId=${user?.userId}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      
      if (historyResponse.ok) {
        const historyData = await historyResponse.json();
        console.log('История данные:', historyData);
        
        if (historyData.success && historyData.data) {
          let historyItems = [];
          
          if (Array.isArray(historyData.data)) {
            // Сортируем историю по дате - новые сверху (последние добавленные сначала)
            historyItems = historyData.data.sort((a, b) => {
              const dateA = new Date(a.matchedAt || a.createdAt || a.timestamp || 0);
              const dateB = new Date(b.matchedAt || b.createdAt || b.timestamp || 0);
              return dateB - dateA; // Сначала новые (большая дата минус меньшая)
            });
            
            console.log('📅 Отсортированная история (новые сверху):', historyItems.map(item => ({
              name: item.name,
              date: item.matchedAt || item.createdAt,
              id: item.recipeId || item.id
            })));
          } else if (historyData.data && typeof historyData.data === 'object') {
            historyItems = [historyData.data];
          }
          
          console.log('Обработанные элементы истории:', historyItems);
          setRecipeHistory(historyItems);
          
          // Загрузка деталей рецептов
          if (historyItems.length > 0) {
            await loadRecipeDetails(historyItems);
          } else {
            setRecipes([]);
          }
        } else {
          setRecipeHistory([]);
          setRecipes([]);
        }
      } else {
        setRecipeHistory([]);
        setRecipes([]);
      }
    } catch (error) {
      console.error('Ошибка загрузки реальных данных:', error);
      // Используем сохраненные данные если есть
      if (recipeHistory.length === 0 && recipes.length === 0) {
        setRecipeHistory([]);
        setRecipes([]);
      }
    }
  };

  // Функция для отображения предпочтений
  const renderPreferences = () => {
    if (!userProfile?.preferences) {
      return (
        <div className="preferences-list">
          <div className="preference-item">
            <span className="preference-label">Предпочтения не настроены</span>
            <span className="preference-value">Нажмите "Изменить" чтобы настроить</span>
          </div>
        </div>
      );
    }

    const { preferences } = userProfile;

    return (
      <div className="preferences-list">
        {/* Тип питания */}
        <div className="preference-item">
          <span className="preference-label">Тип питания:</span>
          <span className="preference-value">
            {preferences.dietType && 
             preferences.dietType !== 'null' && 
             preferences.dietType !== 'undefined' && 
             preferences.dietType.trim() !== '' 
              ? (() => {
                  const display = getPreferenceDisplay(preferences.dietType, 'dietType');
                  return display.emoji ? `${display.emoji} ${display.name}` : display.name;
                })()
              : '🤷 Без предпочтений'
            }
          </span>
        </div>

        {/* Аллергии */}
        <div className="preference-item">
          <span className="preference-label">Аллергия на:</span>
          <span className="preference-value">
            {preferences.allergies && preferences.allergies.length > 0 && 
             !preferences.allergies.includes('NO_ALLERGIES') &&
             !preferences.allergies.some(a => a === null || a === '')
              ? preferences.allergies.map(allergy => {
                  const display = getPreferenceDisplay(allergy, 'allergy');
                  return display.emoji ? `${display.emoji} ${display.name}` : display.name;
                }).join(', ')
              : '❌ Нет аллергий'
            }
          </span>
        </div>

        {/* Исключенные ингредиенты */}
        <div className="preference-item">
          <span className="preference-label">Исключить:</span>
          <span className="preference-value">
            {preferences.excludedIngredients && preferences.excludedIngredients.length > 0 && 
             !preferences.excludedIngredients.includes('NOTHING') &&
             !preferences.excludedIngredients.some(i => i === null || i === '')
              ? preferences.excludedIngredients.map(ingredient => {
                  const display = getPreferenceDisplay(ingredient, 'excludedIngredient');
                  return display.emoji ? `${display.emoji} ${display.name}` : display.name;
                }).join(', ')
              : '❌ Ничего не исключаю'
            }
          </span>
        </div>
      </div>
    );
  };

  const handleStartQuestionnaire = async () => {
    setShowQuestionnaire(true);
  };

  const handleSavePreferences = async (preferences) => {
    try {
      const userId = user?.userId || localStorage.getItem('userId') || '1';
      
      const response = await fetch(`${API_ENDPOINTS.USERS.PREFERENCES}?userId=${userId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(preferences)
      });
      
      if (response.ok) {
        const data = await response.json();
        console.log('✅ Ответ от сервера после сохранения:', data);
        if (data.success) {
          setUserProfile(prev => ({
            ...prev,
            preferences: data.data
          }));
          setShowPreferenceTest(false);
        }
      }
    } catch (error) {
      console.error('Ошибка сохранения предпочтений:', error);
    }
  };

  const handleAddToHistory = async (recipeId) => {
    try {
      const userId = user?.userId || localStorage.getItem('userId') || '1';
      
      console.log('🔍 Добавление рецепта в историю:', {
        userId: userId,
        recipeId: recipeId,
        recipeName: 'Неизвестно'
      });
      
      const response = await fetch(`${API_ENDPOINTS.USERS.HISTORY}?userId=${userId}&recipeId=${recipeId}`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      
      if (response.ok) {
        console.log('✅ Рецепт добавлен в историю с ID:', recipeId);
        // Перезагружаем историю
        await loadRealData();
      } else {
        console.error('❌ Ошибка добавления в историю:', response.status);
        const errorText = await response.text();
        console.error('Текст ошибки:', errorText);
      }
    } catch (error) {
      console.error('❌ Ошибка добавления в историю:', error);
    }
  };

  // В родительском компоненте (например, Main.js)
  const handleRateRecipe = async (recipeId, rating) => {
    try {
      console.log('🟡 Оценка рецепта:', { recipeId, rating });
      
      const userId = user?.userId || localStorage.getItem('userId');
      
      const response = await fetch(`http://localhost:8080/api/recipes/${recipeId}/rating?userId=${userId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify({ rating })
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const result = await response.json();
      console.log('✅ Ответ от сервера:', result);
      
      // Обновляем данные после успешной оценки
      await loadRealData();
      
    } catch (error) {
      console.error('❌ Ошибка оценки рецепта:', error);
      throw error; // Пробрасываем ошибку дальше
    }
  };

  const handleLogout = () => {
    // Очищаем все данные при выходе
    localStorage.removeItem('userProfile');
    localStorage.removeItem('recipeHistory');
    localStorage.removeItem('recipes');
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    localStorage.removeItem('user');
    onLogout();
  };

  return (
    <div className="main-page">
      <Header user={user} onLogout={handleLogout} />
      
      <main className="main-content">
        {/* Приветствие и предпочтения */}
        <section className="welcome-section">
          <h1 className="greeting">
            {greeting}, {user?.username}!
          </h1>
          
          <div className="preferences-section">
            <div className="preferences-header">
              <h3>Ваши предпочтения</h3>
              <button 
                className="edit-preferences-btn"
                onClick={() => setShowPreferenceTest(true)}
              >
                ✏️ Изменить
              </button>
            </div>
            {renderPreferences()}
          </div>
        </section>

        {/* Кнопка подбора рецепта */}
        <section className="action-section">
          <button 
            className="find-recipe-btn"
            onClick={handleStartQuestionnaire}
          >
            🍳 Подобрать рецепт
          </button>
          <p className="action-description">
            Ответьте на несколько вопросов и получите идеальный рецепт для вас
          </p>
        </section>

        {/* История рецептов через отдельный компонент */}
        <RecipeHistory 
          recipeHistory={recipeHistory}
          recipes={recipes}
          loading={loading}
          onRateRecipe={handleRateRecipe}
        />

        {/* Модальное окно теста предпочтений */}
        {showPreferenceTest && (
          <PreferenceTest
            user={user}
            currentPreferences={userProfile?.preferences}
            onSave={handleSavePreferences}
            onClose={() => setShowPreferenceTest(false)}
          />
        )}

        {/* Модальное окно опросника для подбора рецепта */}
        {showQuestionnaire && (
          <RecipeQuestionnaire
            user={user}
            onClose={() => setShowQuestionnaire(false)}
            onRecipeFound={(recipe) => {
              // Добавляем рецепт в историю и показываем его
              handleAddToHistory(recipe.id);
              setShowQuestionnaire(false);
            }}
          />
        )}
      </main>
    </div>
  );
};

export default Main;