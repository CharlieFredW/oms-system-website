CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS orders;


CREATE TABLE IF NOT EXISTS orders
(
    id          serial primary key,
    name        text           not null,
    email       text           not null,
    phone       text           not null,
    address     text           not null,
    city        text           not null,
    zip         text           not null,
    country     text           not null,
    created     timestamp with time zone default now(),
    total_price numeric(10, 2) not null,
    status      orderstatus    not null,
    vat         numeric(10, 2) not null

);
