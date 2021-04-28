create table if not exists usr
(
    id             bigserial primary key,
    name           varchar(50) unique not null,
    phone_number   varchar(50) unique not null,
    email          varchar(50) unique not null,
    last_status    VARCHAR(7),
    current_status VARCHAR(7)
);
CREATE SEQUENCE if not exists clients_id_seq START WITH 1 INCREMENT BY 1;