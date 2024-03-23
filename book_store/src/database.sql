CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL CHECK (CHAR_LENGTH(name) >= 2), -- Minimum length constraint for name
                       username VARCHAR(255) UNIQUE NOT NULL CHECK (CHAR_LENGTH(username) >= 2 AND username NOT LIKE '% %'), -- Minimum length constraint and disallow whitespace
                       password VARCHAR(255) NOT NULL CHECK (CHAR_LENGTH(password) >= 2 AND password NOT LIKE '% %') -- Minimum length constraint and disallow whitespace
);

CREATE TABLE books (
                       book_id INT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       genre VARCHAR(255),
                       price DECIMAL(10, 2) NOT NULL,
                       quantity INT NOT NULL,
                       lender_id INT,
                       FOREIGN KEY (lender_id) REFERENCES users(user_id)
);

CREATE TABLE borrowing_requests (
                                    request_id INT AUTO_INCREMENT PRIMARY KEY,
                                    borrower_id INT,
                                    lender_id INT,
                                    book_id INT,
                                    status ENUM('accepted', 'rejected', 'pending') DEFAULT 'pending',
                                    FOREIGN KEY (borrower_id) REFERENCES users(user_id),
                                    FOREIGN KEY (lender_id) REFERENCES users(user_id),
                                    FOREIGN KEY (book_id) REFERENCES books(book_id)
);

CREATE TABLE request_history (
                                 history_id INT AUTO_INCREMENT PRIMARY KEY,
                                 request_id INT,
                                 status ENUM('accepted', 'rejected', 'pending'),
                                 FOREIGN KEY (request_id) REFERENCES borrowing_requests(request_id)
);
