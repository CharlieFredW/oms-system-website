CREATE TABLE products
(
    id    serial primary key,
    stock integer not null,
    title text    not null
);

INSERT INTO products (stock, title)
VALUES ('11', 'Asus Zenbook 2023 15 Inch Pro'),
       ('8', 'Asus Zenbook 2023 13 Inch Pro'),
       ('6', 'Macbook Air 2022 13 Inch'),
       ('2', 'Macbook Pro 2023 16 Inch'),
       ('21', 'Black USB-C Cable'),
       ('25', 'White USB-C Cable'),
       ('4', 'Black USB-C Charger'),
       ('19', 'White USB-C Charger'),
       ('34', 'Green USB-C Charger'),
       ('29', 'White Iphone Fast Charger'),
       ('32', 'Black Iphone Fast Charger'),
       ('50', 'Green Iphone Fast Charger');




