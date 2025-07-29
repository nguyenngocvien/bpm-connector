CREATE TABLE IF NOT EXISTS core_db_config (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    url VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    driver_class_name VARCHAR(100) NOT NULL
);