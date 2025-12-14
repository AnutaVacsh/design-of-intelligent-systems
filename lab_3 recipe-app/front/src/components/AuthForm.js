import React, { useState } from 'react';
import '../styles/AuthForm.css';

const AuthForm = ({ type, onSubmit, loading, error }) => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: ''
  });

  const [errors, setErrors] = useState({});

  const validateField = (name, value) => {
    switch (name) {
      case 'username':
        if (type === 'register' && value.length < 3) {
          return 'Имя пользователя должно быть не менее 3 символов';
        }
        break;
      case 'email':
        if (type === 'register' && !value.includes('@')) {
          return 'Введите корректный email';
        }
        break;
      case 'password':
        if (value.length < 6) {
          return 'Пароль должен быть не менее 6 символов';
        }
        break;
      default:
        return '';
    }
    return '';
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });

    // Валидация в реальном времени
    const error = validateField(name, value);
    setErrors({
      ...errors,
      [name]: error
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    // Финальная валидация
    const newErrors = {};
    if (type === 'register') {
      if (!formData.username) newErrors.username = 'Введите имя пользователя';
      if (!formData.email) newErrors.email = 'Введите email';
    }
    if (!formData.password) newErrors.password = 'Введите пароль';
    
    if (type === 'register') {
      const usernameError = validateField('username', formData.username);
      const emailError = validateField('email', formData.email);
      if (usernameError) newErrors.username = usernameError;
      if (emailError) newErrors.email = emailError;
    }
    
    const passwordError = validateField('password', formData.password);
    if (passwordError) newErrors.password = passwordError;

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    // Подготавливаем данные согласно API контракту
    let submitData;
    if (type === 'login') {
      submitData = {
        username: formData.username, // Для логина используем username как идентификатор
        password: formData.password
      };
    } else {
      submitData = {
        username: formData.username,
        email: formData.email,
        password: formData.password
      };
    }

    onSubmit(submitData);
  };

  const isFormValid = () => {
    if (type === 'login') {
      return formData.username && formData.password && 
             !errors.username && !errors.password;
    } else {
      return formData.username && formData.email && formData.password &&
             !errors.username && !errors.email && !errors.password;
    }
  };

  return (
    <form className="auth-form" onSubmit={handleSubmit}>
      <h2>{type === 'login' ? 'Вход в систему' : 'Регистрация'}</h2>
      
      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <div className="form-group">
        <label>Имя пользователя:</label>
        <input
          type="text"
          name="username"
          value={formData.username}
          onChange={handleChange}
          placeholder={type === 'login' ? 'Введите имя пользователя или email' : 'Придумайте имя пользователя'}
          required
          className={errors.username ? 'error' : ''}
        />
        {errors.username && <span className="field-error">{errors.username}</span>}
      </div>
      
      {type === 'register' && (
        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            placeholder="Введите ваш email"
            required
            className={errors.email ? 'error' : ''}
          />
          {errors.email && <span className="field-error">{errors.email}</span>}
        </div>
      )}
      
      <div className="form-group">
        <label>Пароль:</label>
        <input
          type="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          placeholder="Введите пароль"
          required
          className={errors.password ? 'error' : ''}
        />
        {errors.password && <span className="field-error">{errors.password}</span>}
        {type === 'register' && (
          <div className="password-hint">Пароль должен содержать не менее 6 символов</div>
        )}
      </div>
      
      <button 
        type="submit" 
        className={`submit-btn ${!isFormValid() || loading ? 'disabled' : ''}`}
        disabled={!isFormValid() || loading}
      >
        {loading ? 'Загрузка...' : (type === 'login' ? 'Войти' : 'Зарегистрироваться')}
      </button>
    </form>
  );
};

export default AuthForm;