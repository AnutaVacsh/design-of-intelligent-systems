import React, { useState } from 'react';
import AuthForm from '../components/AuthForm';
import { apiService } from '../services/apiService';
import '../styles/AuthPages.css';

const Register = ({ onRegister, onSwitchToLogin }) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleRegister = async (registerData) => {
  setLoading(true);
  setError('');

  try {
    const response = await apiService.signup(registerData);
    
    if (response.success) {
      // Добавляем token в user data
      const userData = {
        ...response.data,
        token: response.data.token // убедитесь что бэкенд возвращает token
      };
      onRegister(userData);
    } else {
      setError(response.message || 'Ошибка регистрации');
    }
  } catch (err) {
    setError('Сервер недоступен. Проверьте, запущен ли бэкенд');
  } finally {
    setLoading(false);
  }
};

  return (
    <div className="auth-page">
      <div className="auth-container">
        <AuthForm 
          type="register" 
          onSubmit={handleRegister}
          loading={loading}
          error={error}
        />
        <p className="auth-switch">
          Уже есть аккаунт?{' '}
          <span onClick={onSwitchToLogin} className="auth-link">
            Войти
          </span>
        </p>
      </div>
    </div>
  );
};

export default Register;