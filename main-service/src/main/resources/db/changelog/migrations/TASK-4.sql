--liquibase formatted sql

--changeset penkin:TASK-4

ALTER TABLE service_request ADD COLUMN residence_id UUID;

ALTER TABLE service_request
    ADD CONSTRAINT fk_service_request_residence FOREIGN KEY (residence_id) REFERENCES residence (id)
        ON DELETE CASCADE ON UPDATE CASCADE;