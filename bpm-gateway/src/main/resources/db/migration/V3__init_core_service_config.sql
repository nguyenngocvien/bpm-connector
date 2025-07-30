CREATE TABLE IF NOT EXISTS core_service_config (
    id BIGSERIAL PRIMARY KEY,
    service_code VARCHAR(50) NOT NULL UNIQUE,  -- mã duy nhất gọi service
    service_type VARCHAR(20) NOT NULL,         -- REST, SOAP, DB, MAIL, FILE

    -- REST / SOAP / MAIL / FILE config
    target_url TEXT,                           -- REST/SOAP endpoint or MAIL server
    http_method VARCHAR(10),                   -- GET, POST, etc (REST only)
    headers TEXT,                              -- JSON string: {"Authorization":"Bearer xyz",...}
    payload_template TEXT,                     -- JSON/soap/xml/email template
    
    -- DB config
    db_datasource VARCHAR(50),                 -- tên datasource đã khai báo
    sql_statement TEXT,                        -- SQL to run if type=DB
    sql_type VARCHAR(10),                      -- QUERY / UPDATE / PROC

    -- common
    log_enabled BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT TRUE,
    version INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
