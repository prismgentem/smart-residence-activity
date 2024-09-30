# smart-residence-activity

# Тема курсового проекта - "Системы ведения активности в ЖК"

## Описание проекта

Системы ведения активности в ЖК — это веб-приложение для управления активностью жителей жилого комплекса. Приложение предоставляет единый центр связи для жителей, специалистов и управляющей компании, для получение новостей и сервисных услуг. Также планируется интеграция с внешним API для получения мероприятий в городе от сервиса [KudaGo](https://kudago.com).

### Основные функции

#### Для жителей:
- **Просмотр новостей ЖК**: актуальные уведомления от управляющей компании.
- **Вызов специалистов**: возможность оставить заявку на сантехника, электрика и других специалистов.
- **Связь с УК**: простое и быстрое взаимодействие с управляющей компанией по любым вопросам.
- **Мероприятия в городе**: через интеграцию с API KudaGo пользователи смогут просматривать интересные события в городе.

#### Для администратора ЖК:
- **Управление новостями**: добавление и редактирование важной информации для жителей.
- **Контроль заявок**: управление заявками на вызов специалистов, отслеживание их статуса.
- **Управление жителями**: верификация новых пользователей, управление профилями.
- **Массовые уведомления**: отправка новостей и сообщений всем жителям ЖК.

### Архитектура

Приложение разделено на бэкенд и фронтенд (не уверен что успею сделать часть фронтенда):

- **Бэкенд**: Java-приложение с использованием Spring Boot для обработки бизнес-логики, работы с базой данных и интеграции с KudaGo API.
- **Фронтенд**: Планируется использование React + TypeScript для создания пользовательского интерфейса.
- **База данных**: PostgreSQL для хранения информации о пользователях, заявках, новостях и результатах идентификации.
- **Kafka**: Планируется использование Kafka для реализации системы асинхронной передачи сообщений между компонентами.
- **Интеграция с API**: KudaGo API для получения данных о мероприятиях в городе.

### Сервис уведомлений

Для уведомления пользователей о новых событиях, новостях и изменениях статуса заявок планируется использование **сервиса уведомлений**:

- **Email-уведомления**: Для важных событий (например, подтверждение заявки) будут отправляться email-уведомления.
- **Kafka**: События (например, создание заявки, новая новость) будут отправляться в Kafka, которая передаст их в сервис уведомлений для дальнейшей обработки.


