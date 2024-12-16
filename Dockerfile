# Этап сборки
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

# Копируем файлы pom.xml
COPY pom.xml .
COPY email-notification-service/pom.xml email-notification-service/
COPY main-service/pom.xml main-service/
COPY telegram-bot-notification-service/pom.xml telegram-bot-notification-service/

# Загружаем зависимости
RUN mvn dependency:go-offline -B

# Копируем исходный код
COPY email-notification-service/src email-notification-service/src
COPY main-service/src main-service/src
COPY telegram-bot-notification-service/src telegram-bot-notification-service/src

# Собираем проект
RUN mvn clean package -DskipTests

# Этап запуска
FROM openjdk:17-jdk-slim

WORKDIR /app

# Копируем JAR-файлы из этапа сборки
COPY --from=build /app/email-notification-service/target/*.jar /app/email-notification-service.jar
COPY --from=build /app/main-service/target/*.jar /app/main-service.jar
COPY --from=build /app/telegram-bot-notification-service/target/*.jar /app/telegram-bot-notification-service.jar

# Создаем скрипт для запуска нужного модуля
RUN echo '#!/bin/sh' > /app/run.sh && \
    echo 'java -jar /app/$SERVICE_NAME.jar' >> /app/run.sh && \
    chmod +x /app/run.sh

# Запускаем скрипт
CMD ["/app/run.sh"]
