CREATE TABLE isrv_auth (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER'
);

INSERT INTO isrv_auth (username, password, role)
VALUES ('admin', '$2a$10$wA9nUvLmP78bY7j.TSTT7uYpM8CJmLrq.bh9MVOCEZJxOEaRxBiIW', 'ADMIN');
