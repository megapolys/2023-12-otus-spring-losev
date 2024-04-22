create table roles
(
    id        bigserial,
    name      varchar(255) not null unique,
    primary key (id)
);