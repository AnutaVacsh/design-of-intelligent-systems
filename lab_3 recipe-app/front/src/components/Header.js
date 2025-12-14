import React from 'react';
import '../styles/Header.css';

const Header = ({ user, onLogout, activeSection, setActiveSection }) => {
  return (
    <header className="header">
      <div className="header-left">
        <h1>🍳 Кулинарная книга</h1>
      </div>
      <div className="header-right">
        <nav className="nav-menu">
                    
          <button className="logout-btn" onClick={onLogout}>
            Выйти
          </button>
        </nav>
      </div>
    </header>
  );
};

export default Header;