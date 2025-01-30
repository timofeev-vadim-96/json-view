create table if not exists users (
    id bigserial primary key,
    first_name varchar (255) not null,
    second_name varchar (255) not null,
    email varchar (255) not null
);

create table if not exists products (
    id bigserial primary key,
    name varchar (255) not null,
    cost double precision not null
);

create table if not exists orders (
    id bigserial primary key,
    time datetime not null,
    general_cost double precision not null,
    status varchar(255) not null
        constraint order_status_check check (
            status IN ('CREATED', 'IN_PROCESSING', 'BEING_DELIVERED', 'COMPLETED', 'CANCELLED')),
    user_id bigint references users (id)
);

create table if not exists orders_products (
    id bigserial primary key,
    order_id bigint references orders (id),
    product_id bigint references products (id)
);