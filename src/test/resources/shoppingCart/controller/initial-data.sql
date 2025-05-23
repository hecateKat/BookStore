INSERT INTO books VALUES
                      (1, 'Title of first test book', 'My author', '000-00-00000001', 11.11, 0, null , null),
                      (2, 'Title of second test book', 'My author', '000-00-00000002', 22.22, 0, null , null);
INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted) VALUES
    (1, 'example1@example.com', 'example1234', 'ExampleName', 'ExampleSurname', null, false);
INSERT INTO roles (id, name) VALUES
                      (1, 'ROLE_ADMIN'),
                      (2, 'ROLE_MANAGER'),
                      (3, 'ROLE_USER');
INSERT INTO users_roles (user_id, role_id) VALUES (1, 3);
INSERT INTO shopping_carts (id, user_id, is_deleted) VALUES (1, 1, FALSE);