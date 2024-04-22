create table users
(
    id        bigserial,
    user_name varchar(255) not null unique,
    password  varchar(255) not null,
    locked    bool,
    primary key (id)
);