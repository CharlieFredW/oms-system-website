DROP TABLE IF EXISTS order_info;

CREATE TABLE IF NOT EXISTS order_info
(
    id         serial primary key,
    order_id   int REFERENCES orders (id) not null,
    product_id int                        not null,
    /*product_title text  not null,
    color      text  not null,*/
    quantity   int                        not null,
    price      real                       not null
);

-- product_id is a foreign key to the products table (REFERENCES products (id)) when it is created