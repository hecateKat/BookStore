INSERT INTO users VALUES
                      (1, 'example1@example.com', 'example1', 'Jane', 'Female', 'Book 1', 0),
                      (2, 'example@2example.com', 'example2', 'John', 'Male', 'Book 2', 0);
INSERT INTO shopping_carts (id, user_id, is_deleted) VALUES (1, 1, FALSE), (2, 2, FALSE);
INSERT INTO books VALUES
                      (1, 'Book 1', 'Author', '000-00-00000001', 275.05, 0, 'Part 1', 'cover_1.jpg'),
                      (2, 'Book 2', 'Author', '000-00-00000002', 280.45, 0,'Part 2', 'cover_2.jpg'),
                      (3, 'Book 3', 'Author', '000-00-00000003', 310.75, 0,'Part 3', 'cover_3.jpg'),
                      (4, 'Book 4', 'Author', '000-00-00000004', 351.99, 0,'TPart 4', 'cover_4.jpg');
INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity) VALUES
                                                                     (1, 1, 1, 1),
                                                                     (2, 1, 2, 1),
                                                                     (3, 2, 1, 10),
                                                                     (4, 2, 2, 10),
                                                                     (5, 2, 3, 10),
                                                                     (6, 2, 4, 10);