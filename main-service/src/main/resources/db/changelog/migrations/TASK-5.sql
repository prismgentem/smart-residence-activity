--liquibase formatted sql

--changeset penkin:TASK-5

CREATE TABLE residence_news
(
    id                UUID PRIMARY KEY,
    create_date       TIMESTAMP NOT NULL,
    update_date       TIMESTAMP NOT NULL,
    title             TEXT      NOT NULL,
    content           TEXT      NOT NULL,
    created_by        UUID,
    residence_id      UUID      NOT NULL,
    send_notification BOOLEAN   NOT NULL,
    CONSTRAINT fk_residence_news_created_by FOREIGN KEY (created_by) REFERENCES admin (id),
    CONSTRAINT fk_residence_news_residence FOREIGN KEY (residence_id) REFERENCES residence (id)
);

