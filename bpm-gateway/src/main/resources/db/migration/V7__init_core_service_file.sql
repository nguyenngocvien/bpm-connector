CREATE TABLE IF NOT EXISTS core_service_file (
    config_id BIGINT PRIMARY KEY REFERENCES core_service_config(id) ON DELETE CASCADE,
    
    file_path TEXT NOT NULL,                          -- Đường dẫn tuyệt đối/relative, local hoặc URL
    file_action VARCHAR(20) NOT NULL,                 -- READ, WRITE, DELETE, UPLOAD, DOWNLOAD
    
    file_format VARCHAR(20),                          -- CSV, JSON, XML, PDF, ... (optional)
    file_template TEXT,                               -- Mẫu nội dung file (ghi), hoặc parse (đọc)
    
    file_name_pattern VARCHAR(255),                   -- Ví dụ *.csv, invoice_*.pdf (optional)
    file_encoding VARCHAR(50) DEFAULT 'UTF-8',

    extra_config TEXT,                                -- TEXT cấu hình đặc biệt (tùy chọn)

    retry_count INTEGER DEFAULT 0,
    retry_backoff_ms INTEGER DEFAULT 1000,
    timeout_ms INTEGER DEFAULT 5000,

    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);