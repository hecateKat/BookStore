INSERT INTO books VALUES
                      (1, 'First', 'Author 1', '000-00-00000001', 1.11, 0, 'Book 1', 'cover_1.jpg'),
                      (2, 'Second', 'Author 2', '000-00-00000002', 2.22, 0,'Book 2', 'cover_2.jpg'),
                      (3, 'Third', 'Author 3', '000-00-00000003', 3.33, 0,'Book 3', 'cover_3.jpg');
INSERT INTO categories VALUES
                           (1, 'One', 'category 1', 0),
                           (2, 'Two', 'category 2', 0),
                           (3, 'Three', 'category 3', 0);
INSERT INTO books_categories VALUES
                                 (1, 1),
                                 (2, 2),
                                 (3, 3);