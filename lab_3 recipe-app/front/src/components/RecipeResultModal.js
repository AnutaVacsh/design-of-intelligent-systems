import React, { useState } from 'react';
import { getImageUrl } from '../components/imageUtils';

const RecipeResultModal = ({ recipe, sessionHistory, onClose, onRestart, onAcceptRecipe }) => {
  const [imageError, setImageError] = useState(false);
  const [showQuestionHistory, setShowQuestionHistory] = useState(false);

  if (!recipe) return null;

  const handleImageError = () => {
    setImageError(true);
  };

  // Безопасная обработка инструкций
  const renderInstructions = () => {
    if (!recipe.instructions) return null;
    
    if (Array.isArray(recipe.instructions)) {
      return recipe.instructions.map((step, index) => (
        <p> {step}</p>
      ));
    } else if (typeof recipe.instructions === 'string') {
      return recipe.instructions.split('\n').map((step, index) => (
        <p >{step}</p>
      ));
    }
    return null;
  };

  return (
    <div className="recipe-modal-overlay" onClick={onClose}>
      <div className="recipe-modal-content" onClick={(e) => e.stopPropagation()}>
        <button className="modal-close-btn" onClick={onClose}>×</button>
        
        <div className="recipe-modal-header">
          {recipe.imageUrl && !imageError ? (
            <img 
              src={getImageUrl(recipe.imageUrl)} 
              alt={recipe.name} 
              className="recipe-modal-image"
              onError={handleImageError}
            />
          ) : (
            <div className="recipe-image-placeholder">
              🍳
            </div>
          )}
          <div className="recipe-modal-title">
            <h1>🎉 {recipe.name}</h1>
            <div className="recipe-meta-info">
              {recipe.prepTime && recipe.prepTime > 0 && <span>⏱ {recipe.prepTime} мин</span>}
              { <span>🔥 {recipe.cookTime} мин</span>}
              {recipe.servings && <span>👥 {recipe.servings} порций</span>}
              {recipe.difficultyLevel && <span>📊 {recipe.difficultyLevel}</span>}
            </div>
          </div>
        </div>

        <div className="recipe-modal-body">
          {/* Характеристики */}
          <div className="recipe-characteristics-modal">
            {recipe.vegetarian && <span className="char-tag-modal">🥗 Вегетарианское</span>}
            {recipe.vegan && <span className="char-tag-modal">🌱 Веганское</span>}
            {recipe.glutenFree && <span className="char-tag-modal">🌾 Без глютена</span>}
            {recipe.dairyFree && <span className="char-tag-modal">🥛 Без лактозы</span>}
            {recipe.cuisineType && <span className="char-tag-modal">🌍 {recipe.cuisineType}</span>}
          </div>

          {/* Описание */}
          {recipe.description && (
            <div className="recipe-section">
              <h3>Описание</h3>
              <p>{recipe.description}</p>
            </div>
          )}

          {/* История вопросов */}
          {sessionHistory && sessionHistory.length > 0 && (
            <div className="recipe-section">
              <div 
                className="history-toggle"
                onClick={() => setShowQuestionHistory(!showQuestionHistory)}
              >
                <h3>📊 История подбора</h3>
                <span className="toggle-icon">
                  {showQuestionHistory ? '▲' : '▼'}
                </span>
              </div>
              {showQuestionHistory && (
                <div className="question-history">
                  {sessionHistory.map((step, index) => (
                    <div key={index} className="history-step">
                      <div className="step-number">Шаг {index + 1}</div>
                      <div className="step-question"><strong>Вопрос:</strong> {step.question}</div>
                      <div className="step-answer"><strong>Ответ:</strong> {step.answer === 'yes' ? 'Да' : 'Нет'}</div>
                    </div>
                  ))}
                  <div className="history-step final-step">
                    <div className="step-number">🎉 Результат</div>
                    <div className="step-question"><strong>Найден рецепт:</strong> {recipe.name}</div>
                  </div>
                </div>
              )}
            </div>
          )}

          {/* Ингредиенты */}
          {recipe.ingredients && recipe.ingredients.length > 0 && (
            <div className="recipe-section">
              <h3>🛒 Ингредиенты</h3>
              <ul className="ingredients-list">
                {recipe.ingredients.map((ingredient, index) => (
                  <li key={index}>
                    {typeof ingredient === 'string' ? ingredient : ingredient.name}
                    {typeof ingredient === 'object' && ingredient.amount && (
                      <span className="ingredient-amount-black"> - {ingredient.amount}</span>
                    )}
                  </li>
                ))}
              </ul>
            </div>
          )}

          {/* Инструкции */}
          {recipe.instructions && (
            <div className="recipe-section">
              <h3>👨‍🍳 Приготовление</h3>
              <div className="instructions-text">
                {renderInstructions()}
              </div>
            </div>
          )}

          {/* Кнопки действий */}
          <div className="recipe-modal-actions">
            <button className="btn-secondary" onClick={onRestart}>
              🔄 Найти другой рецепт
            </button>
            <button className="btn-primary" onClick={onAcceptRecipe}>
              ✅ Сохранить рецепт
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RecipeResultModal;