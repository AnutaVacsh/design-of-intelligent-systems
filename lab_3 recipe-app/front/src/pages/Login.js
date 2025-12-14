import React, { useState } from 'react';
import AuthForm from '../components/AuthForm';
import { apiService } from '../services/apiService';
import '../styles/AuthPages.css';

const Login = ({ onLogin, onSwitchToRegister }) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleLogin = async (loginData) => {
  setLoading(true);
  setError('');

  try {
    const response = await apiService.login(loginData);
    
    if (response.success) {
      // Добавляем token в user data
      const userData = {
        ...response.data,
        token: response.data.token // убедитесь что бэкенд возвращает token
      };
      onLogin(userData);
    } else {
      setError(response.message || 'Ошибка авторизации');
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
          type="login" 
          onSubmit={handleLogin}
          loading={loading}
          error={error}
        />
        <p className="auth-switch">
          Нет аккаунта?{' '}
          <span onClick={onSwitchToRegister} className="auth-link">
            Зарегистрироваться
          </span>
        </p>
      </div>
    </div>
  );
};

export default Login;