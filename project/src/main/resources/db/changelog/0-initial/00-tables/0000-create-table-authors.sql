create table chat_user
(
    id        bigint,
    login     varchar(255) not null unique,
    type      varchar(16) not null,
    primary key (id)
);