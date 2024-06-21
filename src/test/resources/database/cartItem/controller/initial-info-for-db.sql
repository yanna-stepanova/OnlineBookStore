INSERT INTO books VALUES
 (1, 'Title of first test book', 'My author', '000-00-00000001', 11.11, 0, null , null),
 (2, 'Title of second test book', 'My author', '000-00-00000002', 22.22, 0, null , null);
INSERT INTO users VALUES (1, 'alice@example.com', 'alice1234', 'Alice', 'Pink', null, 0);
INSERT INTO roles VALUES
 (1, 'ROLE_ADMIN'),
 (2, 'ROLE_MANAGER'),
 (3, 'ROLE_USER');
INSERT INTO users_roles VALUES (1, 3);
INSERT INTO shopping_carts VALUES (1, 0);
