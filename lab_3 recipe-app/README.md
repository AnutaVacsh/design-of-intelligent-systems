# Экспертная система подбора рецептов
Экспертная система для автоматического подбора кулинарных рецептов на основе предпочтений и ограничений пользователя. Система использует диалоговый подход для определения вкусовых предпочтений, диетических ограничений и кулинарных навыков пользователя, после чего рекомендует наиболее подходящие рецепты.

## Технологический стек
Backend
- Java 17 + Spring Boot
- PostgreSQL - база данных
- Liquibase - управление миграциями БД
- SpringDoc OpenAPI - документация API

- React 18 - пользовательский интерфейс
- CSS3 - стилизация

- Docker - контейнеризация

## Начало работы
```bash
# Клонировать репо
git clone [url]
cd recipe-app

# Запустить все сервисы
docker-compose up -d
```

## Frontend (Пользовательский интерфейс)
- URL: http://localhost:3000
- Описание: Веб-интерфейс для взаимодействия с системой подбора рецептов

## Backend API
- URL: http://localhost:8080
- Документация API: http://localhost:8080/swagger-ui.html
- JSON API docs: http://localhost:8080/api-docs

## База данных
- Host: localhost:5432
- Database: cooking_expert_system
- User: postgres
- Password: postgres