--liquibase formatted sql

--changeset penkin:TASK-3

ALTER TABLE residence ADD COLUMN lon NUMERIC;
ALTER TABLE residence ADD COLUMN lat NUMERIC;