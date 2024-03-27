create table books
(
    id        bigserial,
    title     varchar(255),
    author_id bigint references authors (id),
    primary key (id)
);