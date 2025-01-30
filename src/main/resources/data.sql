INSERT INTO users (first_name, second_name, email)
VALUES ('Alice', 'Smith', 'alice.smith@example.com'),
       ('Bob', 'Johnson', 'bob.johnson@example.com'),
       ('Charlie', 'Brown', 'charlie.brown@example.com'),
       ('Diana', 'Prince', 'diana.prince@example.com'),
       ('Edward', 'Norton', 'edward.norton@example.com');

INSERT INTO products (name, cost)
VALUES ('Laptop', 999.99),
       ('Smartphone', 499.99),
       ('Headphones', 89.99),
       ('Smartwatch', 199.99),
       ('Tablet', 299.99);

INSERT INTO orders (time, general_cost, status, user_id)
VALUES (NOW(), 1500.00, 'CREATED', 1),         -- заказ для Alice
       (NOW(), 750.50, 'COMPLETED', 2),        -- заказ для Bob
       (NOW(), 300.75, 'IN_PROCESSING', 3),      -- заказ для Charlie
       (NOW(), 1200.00, 'BEING_DELIVERED', 4), -- заказ для Diana
       (NOW(), 449.99, 'CANCELLED', 5); -- заказ для Edward

INSERT INTO orders_products (order_id, product_id)
VALUES (1, 1), -- Alice's order includes a Laptop
       (1, 2), -- Alice's order also includes a Smartphone
       (2, 3), -- Bob's order includes Headphones
       (3, 4), -- Charlie's order includes a Smartwatch
       (4, 5), -- Diana's order includes a Tablet
       (5, 1), -- Edward's order includes a Laptop
       (2, 4); -- Bob also ordered a Smartwatch
