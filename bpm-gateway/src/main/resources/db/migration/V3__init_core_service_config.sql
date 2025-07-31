CREATE TABLE IF NOT EXISTS core_service_config (
    id BIGSERIAL PRIMARY KEY,
    
    service_code VARCHAR(50) NOT NULL UNIQUE,         -- mã duy nhất gọi service
    service_name VARCHAR(100) NOT NULL,               -- tên mô tả ngắn gọn
    service_description TEXT,                         -- mô tả chi tiết chức năng service
    
    service_type VARCHAR(20) NOT NULL,                -- ENUM: REST, SOAP, DB, MAIL, FILE
    
    log_enabled BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT TRUE,
    version INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE service_type_enum AS ENUM ('REST', 'SOAP', 'DB', 'MAIL', 'FILE');

ALTER TABLE core_service_config
  ALTER COLUMN service_type TYPE service_type_enum USING service_type::service_type_enum;
