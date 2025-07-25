CREATE TABLE IF NOT EXISTS isrv_config (
    id BIGSERIAL PRIMARY KEY,
    service_code VARCHAR(50) NOT NULL UNIQUE,
    target_url TEXT NOT NULL,
    http_method VARCHAR(10),
    headers TEXT,
    payload_mapping TEXT,
    active BOOLEAN DEFAULT TRUE,
    log_enabled BOOLEAN DEFAULT FALSE,
    version INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
