import React, { useState, useEffect } from 'react';
import Login from './pages/Login';
import Register from './pages/Register';
import Main from './pages/Main';
import './styles/App.css';

function App() {
  const [user, setUser] = useState(null);
  const [currentView, setCurrentView] = useState('login');
  const [loading, setLoading] = useState(true);

  // Восстанавливаем состояние при загрузке приложения
  useEffect(() => {
    const savedUser = localStorage.getItem('user');
    const savedToken = localStorage.getItem('token');
    
    if (savedUser && savedToken) {
      try {
        const userData = JSON.parse(savedUser);
        setUser(userData);
        console.log('🔑 Восстановлен пользователь:', userData.username);
      } catch (error) {
        console.error('Ошибка восстановления пользователя:', error);
        clearAuthData();
      }
    }
    
    setLoading(false);
  }, []);

  const clearAuthData = () => {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    setUser(null);
  };

  const handleLogin = (userData) => {
    setUser(userData);
    // Сохраняем все данные в localStorage
    localStorage.setItem('user', JSON.stringify(userData));
    localStorage.setItem('token', userData.token);
    localStorage.setItem('userId', userData.userId);
    localStorage.setItem('username', userData.username);
    console.log('🔑 Пользователь сохранен в localStorage');
  };

  const handleRegister = (userData) => {
    console.log('Пользователь зарегистрирован:', userData);
    setCurrentView('login');
  };

  const handleLogout = () => {
    setUser(null);
    clearAuthData();
    console.log('🔑 Пользователь вышел из системы');
  };

  const switchToRegister = () => {
    setCurrentView('register');
  };

  const switchToLogin = () => {
    setCurrentView('login');
  };

  // Показываем загрузку пока восстанавливаем состояние
  if (loading) {
    return (
      <div className="App">
        <div className="loading-screen">
          <div className="loading-spinner"></div>
          <p>Загрузка...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="App">
      {!user ? (
        currentView === 'login' ? (
          <Login 
            onLogin={handleLogin} 
            onSwitchToRegister={switchToRegister}
          />
        ) : (
          <Register 
            onRegister={handleRegister}
            onSwitchToLogin={switchToLogin}
          />
        )
      ) : (
        <Main user={user} onLogout={handleLogout} />
      )}
    </div>
  );
}

export default App;