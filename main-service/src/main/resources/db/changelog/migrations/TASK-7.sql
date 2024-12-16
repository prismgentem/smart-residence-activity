--liquibase formatted sql

--changeset penkin:TASK-7

CREATE TABLE subscriber
(
    id                UUID PRIMARY KEY,
    create_date       TIMESTAMP NOT NULL,
    update_date       TIMESTAMP NOT NULL,
    email             TEXT      NOT NULL,
    telegram_username TEXT      NOT NULL,
    residence_id      UUID      NOT NULL
);