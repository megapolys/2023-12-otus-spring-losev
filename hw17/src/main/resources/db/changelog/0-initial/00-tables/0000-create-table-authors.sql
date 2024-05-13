create table authors
(
    id        bigserial,
    full_name varchar(255) not null unique,
    primary key (id)
);