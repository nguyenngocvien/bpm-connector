CREATE TABLE IF NOT EXISTS core_service_db (
    id BIGINT PRIMARY KEY REFERENCES core_service_config(id) ON DELETE CASCADE,

    ds_id VARCHAR(50) NOT NULL,					   -- Datasource ID
    sql_statement TEXT NOT NULL,                   -- câu SQL cụ thể
    sql_type VARCHAR(10) NOT NULL,                 -- QUERY / UPDATE / PROC

    input_params TEXT,                            -- JSON hoặc comma-separated (nếu không dùng JSON)
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