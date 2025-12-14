// Типы для API контрактов
export const AuthAPI = {
  // Регистрация
  signup: {
    endpoint: '/api/auth/signup',
    method: 'POST',
    request: {
      username: '',
      email: '',
      password: ''
    },
    response: {
      success: false,
      message: '',
      data: {
        token: '',
        userId: 0,
        username: ''
      }
    }
  },
  
  // Вход
  login: {
    endpoint: '/api/auth/login',
    method: 'POST', 
    request: {
      username: '',
      password: ''
    },
    response: {
      success: false,
      message: '',
      data: {
        token: '',
        userId: 0,
        username: ''
      }
    }
  }
};