--liquibase formatted sql

--changeset penkin:TASK-6

ALTER TABLE users ADD COLUMN telegram_username TEXT;