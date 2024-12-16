--liquibase formatted sql

--changeset penkin:dummy-data

INSERT INTO residence (id, create_date, update_date, name, address, management_company, lon, lat)
VALUES ('1c771d0a-6c93-4c1b-88f0-f83ab3ab79c1', NOW(), NOW(), 'ЖК Солнечный', 'ул. Ленина, д. 10', 'УК Солнечный Дом',
        37.6173, 55.7558),
       ('8c3d1e62-6f98-4d7b-8d50-1f8c02377141', NOW(), NOW(), 'ЖК Зелёная Роща', 'ул. Победы, д. 15', 'УК Комфорт',
        37.5195, 55.7417),
       ('3f456ae1-5f82-48b7-a23c-dae80f65ab29', NOW(), NOW(), 'ЖК Речной Берег', 'ул. Кутузова, д. 3', 'УК Надежда',
        37.6658, 55.7999);

INSERT INTO users (id, create_date, update_date, first_name, last_name, second_name, email, phone_number, verified,
                   residence_id)
VALUES ('1a14d01c-07b3-46f5-9231-1a8f364e93b1', NOW(), NOW(), 'Иван', 'Иванов', 'Сергеевич', 'ivanov@mail.ru',
        '+79160000001', true, '1c771d0a-6c93-4c1b-88f0-f83ab3ab79c1'),
       ('2b3f1a6e-dc29-4b83-934b-df38b9ac7419', NOW(), NOW(), 'Мария', 'Петрова', 'Ивановна', 'petrova@mail.ru',
        '+79160000002', false, '8c3d1e62-6f98-4d7b-8d50-1f8c02377141'),
       ('3c27f28a-1d43-4b09-b76a-c934e29e6c4a', NOW(), NOW(), 'Сергей', 'Сидоров', 'Анатольевич', 'sidorov@mail.ru',
        '+79160000003', true, '3f456ae1-5f82-48b7-a23c-dae80f65ab29'),
       ('4d14a02b-07c4-46f5-9332-1a8f364f92b2', NOW(), NOW(), 'Елена', 'Кузнецова', 'Викторовна', 'kuznetsova@mail.ru',
        '+79160000004', true, '1c771d0a-6c93-4c1b-88f0-f83ab3ab79c1'),
       ('5e24b14d-dc30-4b94-935c-df38b9bc7420', NOW(), NOW(), 'Алексей', 'Морозов', 'Николаевич', 'morozov@mail.ru',
        '+79160000005', true, '8c3d1e62-6f98-4d7b-8d50-1f8c02377141'),
       ('6f35c15e-1d44-4b1a-b77b-c934e29f6c5b', NOW(), NOW(), 'Ольга', 'Смирнова', 'Андреевна', 'smirnova@mail.ru',
        '+79160000006', false, '3f456ae1-5f82-48b7-a23c-dae80f65ab29');


INSERT INTO admin (id, create_date, update_date, first_name, last_name, second_name, email, phone_number, residence_id)
VALUES ('4d42f3e7-8c6c-45bf-9084-cc3d8202a4ad', NOW(), NOW(), 'Анна', 'Васильева', 'Петровна', 'vasilieva@mail.ru',
        '+79160000004', '1c771d0a-6c93-4c1b-88f0-f83ab3ab79c1'),
       ('5a52f3e7-866c-45bf-9084-cc3d8202a4ad', NOW(), NOW(), 'Алина', 'Жребченко', 'Сергеевна', 'shrebchenco@mail.ru',
        '+79160000004', '8c3d1e62-6f98-4d7b-8d50-1f8c02377141'),
       ('5e77c9e9-29a6-4f47-b749-f47a8e37e821', NOW(), NOW(), 'Дмитрий', 'Кузнецов', 'Александрович',
        'kuznetsov@mail.ru', '+79160000005', '3f456ae1-5f82-48b7-a23c-dae80f65ab29');

INSERT INTO service_request (id, create_date, update_date, service_category, service_type, status, description,
                             user_id)
VALUES ('6f6e847b-d29a-4f97-914e-f2cb546a9d34', NOW(), NOW(), 'LIFTING_EQUIPMENT', 'PAID', 'В ожидании',
        'Течет кран на кухне',
        '1a14d01c-07b3-46f5-9231-1a8f364e93b1'),
       ('7e7d846b-f39b-4f37-834f-fc2d7167c824', NOW(), NOW(), 'FIRE_SITUATION', 'EMERGENCY', 'Завершено',
        'Нет света в комнате',
        '2b3f1a6e-dc29-4b83-934b-df38b9ac7419'),
       ('8c8f937d-c38d-4d67-837f-ce3d7176c934', NOW(), NOW(), 'ELECTRICITY', 'PAID', 'В процессе',
        'Не убрана территория у подъезда', '3c27f28a-1d43-4b09-b76a-c934e29e6c4a');
