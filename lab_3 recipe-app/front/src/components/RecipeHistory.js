import React, { useState } from 'react';
import { getImageUrl } from './imageUtils';
import RecipeModal from './RecipeModal';
import '../styles/RecipeHistory.css';

const RecipeHistory = ({ recipeHistory, recipes, loading, onRateRecipe }) => {
  const [selectedRecipe, setSelectedRecipe] = useState(null);
  const [ratingRecipe, setRatingRecipe] = useState(null);
  const [ratingValue, setRatingValue] = useState(0);
  
  console.log('📋 RecipeHistory получил данные:', {
    recipeHistoryCount: recipeHistory?.length,
    recipesCount: recipes?.length,
    recipesOrder: recipes?.map(r => ({ name: r.name, date: r.matchedAt, id: r.id }))
  });
  
  if (loading) {
    return <div className="loading">Загрузка истории...</div>;
  }

  const hasHistory = recipeHistory && recipeHistory.length > 0;
  const hasRecipes = recipes && recipes.length > 0;
  
  // Логируем порядок отображения
  const displayRecipes = hasRecipes ? recipes : recipeHistory;
  
  console.log('👀 Будут отображены рецепты в порядке:', displayRecipes.map(r => ({
    name: r.name, 
    date: r.matchedAt || r.originalMatchedAt,
    id: r.id
  })));

  if (!hasHistory && !hasRecipes) {
    return (
      <div className="empty-history">
        <p>У вас еще нет истории рецептов</p>
        <p className="empty-subtitle">Найденные рецепты будут появляться здесь</p>
      </div>
    );
  }

  const handleRecipeClick = (recipe) => {
    setSelectedRecipe(recipe);
  };

  const handleRateClick = (recipe, event) => {
    event.stopPropagation();
    setRatingRecipe(recipe);
    setRatingValue(recipe.rating || 0);
  };

  const handleRatingSubmit = async () => {
    if (ratingRecipe && ratingValue > 0) {
      await onRateRecipe(ratingRecipe.id, ratingValue);
      setRatingRecipe(null);
      setRatingValue(0);
    }
  };

  const handleCloseModal = () => {
    setSelectedRecipe(null);
  };

  // Функция для форматирования даты
  const formatDate = (dateString) => {
    if (!dateString) return 'Недавно';
    
    const date = new Date(dateString);
    const now = new Date();
    
    // Сбрасываем время для корректного сравнения дат
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const targetDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());
    
    const diffTime = Math.abs(today - targetDate);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays === 0) return 'Сегодня';
    if (diffDays === 1) return 'Вчера';
    if (diffDays === 2) return 'Позавчера';
    if (diffDays <= 7) return `${diffDays} дней назад`;
    
    return date.toLocaleDateString('ru-RU', {
        day: 'numeric',
        month: 'long',
        year: 'numeric'
    });
};

  return (
    <>
      <div className="history-section">
        <h2>История рецептов ({displayRecipes.length})</h2>
        
        <div className="recipes-history">
          {displayRecipes.map((item, index) => {
            const recipe = item;
            
            return (
              <div 
                key={recipe.historyId || recipe.recipeId || recipe.id || index} 
                className="recipe-history-card"
                onClick={() => handleRecipeClick(recipe)}
              >
                <div className="recipe-image">
                  {recipe.imageUrl ? (
                    <img 
                      src={getImageUrl(recipe.imageUrl)} 
                      alt={recipe.name} 
                      onError={(e) => {
                        e.target.style.display = 'none';
                        e.target.nextSibling.style.display = 'flex';
                      }}
                    />
                  ) : (
                    <div className="image-placeholder">🍳</div>
                  )}
                  {recipe.imageUrl && (
                    <div className="image-placeholder" style={{ display: 'none' }}>🍳</div>
                  )}
                </div>
                
                <div className="recipe-info">
                  <h3 className="recipe-name">{recipe.name || 'Без названия'}</h3>
                  
                  <div className="recipe-characteristics">
                    {recipe.vegetarian && <span className="char-tag">🥗 Вегетарианское</span>}
                    {recipe.vegan && <span className="char-tag">🌱 Веганское</span>}
                    {recipe.glutenFree && <span className="char-tag">🌾 Без глютена</span>}
                    {recipe.dairyFree && <span className="char-tag">🥛 Без лактозы</span>}
                    {recipe.prepTime && <span className="char-tag">⏱ {recipe.prepTime} мин</span>}
                {<span className="char-tag">🔥 {recipe.cookTime} мин</span>}
                    {recipe.difficultyLevel && <span className="char-tag">📊 {recipe.difficultyLevel}</span>}
                  </div>
                  
                  {recipe.ingredients && recipe.ingredients.length > 0 && (
                    <div className="recipe-ingredients">
                      <strong>Ингредиенты:</strong>{' '}
                      {recipe.ingredients.slice(0, 3).map(ing => 
                        typeof ing === 'string' ? ing : ing.name
                      ).join(', ')}
                      {recipe.ingredients.length > 3 && '...'}
                    </div>
                  )}
                  
                  {recipe.description && (
                    <p className="recipe-description">
                      {recipe.description.length > 100 
                        ? `${recipe.description.substring(0, 100)}...` 
                        : recipe.description
                      }
                    </p>
                  )}
                </div>
                
                <div className="recipe-meta">
                  <div className="recipe-date">
                    {formatDate(recipe.matchedAt || recipe.createdAt)}
                  </div>
                  <div 
                    className="recipe-rating"
                    onClick={(e) => handleRateClick(recipe, e)}
                  >
                    {recipe.rating ? '⭐'.repeat(recipe.rating) : '☆ Оценить'}
                    {recipe.rating > 0 && <span className="rating-text">({recipe.rating})</span>}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* Модальное окно с рецептом */}
      {selectedRecipe && (
        <RecipeModal 
          recipe={selectedRecipe} 
          onClose={handleCloseModal} 
          onRate={onRateRecipe}
        />
      )}

      {/* Модальное окно оценки */}
      {ratingRecipe && (
        <div className="rating-modal-overlay">
          <div className="rating-modal">
            <div className="rating-content">
              <h3>Оцените рецепт</h3>
              <p>{ratingRecipe.name}</p>
              
              <div className="stars-rating">
                {[1, 2, 3, 4, 5].map(star => (
                  <button
                    key={star}
                    className={`star ${star <= ratingValue ? 'active' : ''}`}
                    onClick={() => setRatingValue(star)}
                  >
                    ⭐
                  </button>
                ))}
              </div>
              
              <div className="rating-actions">
                <button 
                  className="btn-secondary"
                  onClick={() => setRatingRecipe(null)}
                >
                  Отмена
                </button>
                <button 
                  className="btn-primary"
                  onClick={handleRatingSubmit}
                  disabled={ratingValue === 0}
                >
                  Сохранить оценку
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default RecipeHistory;