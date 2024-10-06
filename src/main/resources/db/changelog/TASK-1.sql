--liquibase formatted sql

--changeset penkin:TASK-1

CREATE TABLE residence
(
    id                 UUID PRIMARY KEY,
    create_date        TIMESTAMP NOT NULL,
    update_date        TIMESTAMP NOT NULL,
    name               TEXT      NOT NULL,
    address            TEXT      NOT NULL,
    management_company TEXT
);

CREATE TABLE users
(
    id           UUID PRIMARY KEY,
    create_date  TIMESTAMP NOT NULL,
    update_date  TIMESTAMP NOT NULL,
    first_name   TEXT      NOT NULL,
    last_name    TEXT      NOT NULL,
    second_name  TEXT,
    email        TEXT      NOT NULL,
    phone_number TEXT,
    verified     BOOLEAN   NOT NULL,
    residence_id UUID,
    CONSTRAINT fk_user_residence FOREIGN KEY (residence_id) REFERENCES residence (id)
);

CREATE TABLE admin
(
    id           UUID PRIMARY KEY,
    create_date  TIMESTAMP NOT NULL,
    update_date  TIMESTAMP NOT NULL,
    first_name   TEXT      NOT NULL,
    last_name    TEXT      NOT NULL,
    second_name  TEXT,
    email        TEXT      NOT NULL,
    phone_number TEXT,
    residence_id UUID,
    CONSTRAINT fk_admin_residence FOREIGN KEY (residence_id) REFERENCES residence (id)
);

CREATE TABLE request
(
    id           UUID PRIMARY KEY,
    create_date  TIMESTAMP NOT NULL,
    update_date  TIMESTAMP NOT NULL,
    service_type TEXT      NOT NULL,
    status       TEXT      NOT NULL,
    description  TEXT,
    user_id      UUID,
    CONSTRAINT fk_request_user FOREIGN KEY (user_id) REFERENCES users (id)
);