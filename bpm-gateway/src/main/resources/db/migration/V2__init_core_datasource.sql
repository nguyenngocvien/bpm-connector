CREATE TABLE IF NOT EXISTS core_datasource (
    id SERIAL PRIMARY KEY,
    
    name VARCHAR(100) UNIQUE NOT NULL,              -- tên định danh
    description TEXT,                               -- mô tả datasource (optional)

    url VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,                 -- nên mã hóa hoặc để trống nếu dùng vault

    driver_class_name VARCHAR(100) NOT NULL,        -- ví dụ: org.postgresql.Driver

    -- Pool settings (optional, override default nếu cần)
    max_pool_size INTEGER DEFAULT 10,
    min_idle INTEGER DEFAULT 2,
    connection_timeout_ms INTEGER DEFAULT 30000,    -- milliseconds
    idle_timeout_ms INTEGER DEFAULT 600000,
    max_lifetime_ms INTEGER DEFAULT 1800000,

    -- Bổ sung: audit và trạng thái
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
