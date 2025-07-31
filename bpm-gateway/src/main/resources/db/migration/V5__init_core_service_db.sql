CREATE TABLE IF NOT EXISTS core_service_db (
    id BIGINT PRIMARY KEY REFERENCES core_service_config(id) ON DELETE CASCADE,

    db_datasource VARCHAR(50) NOT NULL,            -- tên datasource đã khai báo
    sql_statement TEXT NOT NULL,                   -- câu SQL cụ thể
    sql_type VARCHAR(10) NOT NULL,                 -- QUERY / UPDATE / PROC

    -- Bổ sung cột để flexible hơn
    input_params TEXT,                             -- JSON hoặc comma-separated (nếu không dùng JSON)
    output_mapping TEXT,                           -- mapping kết quả trả về (nếu QUERY)
    timeout_ms INTEGER DEFAULT 3000,               -- timeout cho câu SQL
    retry_count INTEGER DEFAULT 0,                 -- retry nếu lỗi
    retry_backoff_ms INTEGER DEFAULT 1000,         -- thời gian delay giữa các lần retry

    transactional BOOLEAN DEFAULT FALSE,           -- TRUE: wrap SQL trong transaction
    fetch_size INTEGER DEFAULT 100,                -- batch fetch (nếu nhiều record)
    
    result_type VARCHAR(20) DEFAULT 'LIST',  -- LIST, SINGLE, NONE
    enabled BOOLEAN DEFAULT TRUE,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE core_service_db_param (
    id SERIAL PRIMARY KEY,
    db_config_id BIGINT REFERENCES core_service_db(id) ON DELETE CASCADE,
    
    param_name VARCHAR(100) NOT NULL,
    param_type VARCHAR(20),                  -- STRING, INT, DATE,...
    param_mode VARCHAR(10) DEFAULT 'IN',      -- IN, OUT (cho stored proc)
    param_order INTEGER DEFAULT 0
);

CREATE TABLE core_service_db_output_map (
    id SERIAL PRIMARY KEY,
    db_config_id BIGINT REFERENCES core_service_db(id) ON DELETE CASCADE,

    column_name VARCHAR(100) NOT NULL,
    output_field VARCHAR(100) NOT NULL       -- tên key trả về
);