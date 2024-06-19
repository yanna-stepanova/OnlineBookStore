INSERT INTO users VALUES
 (1, 'molly@example.com', 'molly1234', 'Molly', 'Female', 'City, st. Green, 27', 0),
 (2, 'bruce@example.com', 'bruce1234', 'Bruce', 'Male', 'Village, Chornovola Avenue, 9', 0);
INSERT INTO shopping_carts VALUES (1, 0), (2,0);
INSERT INTO books VALUES
 (1, 'Cinder', 'Marissa Meyer', '000-00-00000001', 275.05, 0, 'The Lunar Chronicles. Part 1', 'http://example.com/cover_Cinder.jpg'),
 (2, 'Scarlet', 'Marissa Meyer', '000-00-00000002', 280.45, 0,'The Lunar Chronicles. Part 2', 'http://example.com/cover_Scarlet.jpg'),
 (3, 'Cress', 'Marissa Meyer', '000-00-00000003', 310.75, 0,'The Lunar Chronicles. Part 3', 'http://example.com/cover_Cress.jpg'),
 (4, 'Winter', 'Marissa Meyer', '000-00-00000004', 351.99, 0,'The Lunar Chronicles. Part 4', 'http://example.com/cover_Winter.jpg');
INSERT INTO cart_items VALUES
 (1, 1, 1, 1),
 (2, 1, 2, 1),
 (3, 2, 1, 10),
 (4, 2, 2, 10),
 (5, 2, 3, 10),
 (6, 2, 4, 10);
