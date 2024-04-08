create table users_roles
(
    user_id  bigint references users (id) on delete cascade,
    role_id  bigint references roles (id) on delete cascade,
    primary key (user_id, role_id)
);