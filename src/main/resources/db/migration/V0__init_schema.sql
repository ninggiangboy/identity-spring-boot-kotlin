create extension if not exists "uuid-ossp";

create table app_user
(
    user_id       uuid primary key default uuid_generate_v4(),
    username      text not null unique,
    hash_password text not null,
    email         text not null unique,
    is_enabled    boolean          default false,
    created_at    timestamp        default now()
);