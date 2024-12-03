--liquibase formatted sql

--changeset penkin:TASK-2

ALTER TABLE service_request ADD COLUMN service_category TEXT NOT NULL;