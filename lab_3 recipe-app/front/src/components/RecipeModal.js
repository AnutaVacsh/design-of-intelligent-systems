import React, { useState } from 'react';
import { getImageUrl } from '../components/imageUtils';
import '../styles/RecipeModal.css';

const RecipeModal = ({ recipe, onClose }) => {
  const [imageError, setImageError] = useState(false);

  if (!recipe) return null;

  const handleImageError = () => {
    setImageError(true);
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
            <h1>{recipe.name}</h1>
            <div className="recipe-meta-info">
              {recipe.prepTime && <span>⏱ {recipe.prepTime} мин</span>}
              { <span>🍳 {recipe.cookTime} мин</span>}
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

          {/* Ингредиенты */}
          {recipe.ingredients && recipe.ingredients.length > 0 && (
            <div className="recipe-section">
              <h3>Ингредиенты</h3>
              <ul className="ingredients-list">
                {recipe.ingredients.map((ingredient, index) => (
                  <li key={index}>
                    {typeof ingredient === 'string' ? ingredient : ingredient.name}
                    {typeof ingredient === 'object' && ingredient.amount && (
                      <span className="ingredient-amount"> - {ingredient.amount}</span>
                    )}
                  </li>
                ))}
              </ul>
            </div>
          )}

          {/* Инструкции */}
          {recipe.instructions && (
            <div className="recipe-section">
              <h3>Инструкции приготовления</h3>
              <div className="instructions-text">
                {recipe.instructions.split('\n').map((step, index) => (
                  <p key={index}>{step}</p>
                ))}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default RecipeModal;