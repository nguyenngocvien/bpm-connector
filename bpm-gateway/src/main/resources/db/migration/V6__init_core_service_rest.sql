CREATE TABLE core_service_rest (
    id BIGINT PRIMARY KEY REFERENCES core_service_config(id) ON DELETE CASCADE,

    target_url TEXT NOT NULL,
    http_method VARCHAR(10) NOT NULL DEFAULT 'GET',
    content_type VARCHAR(50) DEFAULT 'application/json',

    timeout_ms INTEGER DEFAULT 3000,
    retry_count INTEGER DEFAULT 0,
    retry_backoff_ms INTEGER DEFAULT 1000,

    payload_template TEXT,
    response_mapping TEXT,

    auth_id INTEGER REFERENCES core_service_auth(id) ON DELETE SET NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE core_service_rest_header (
    id SERIAL PRIMARY KEY,
    rest_config_id BIGINT REFERENCES core_service_rest(id) ON DELETE CASCADE,
    
    header_name VARCHAR(100) NOT NULL,
    header_value VARCHAR(500) NOT NULL
);

CREATE TABLE core_service_rest_query_param (
    id SERIAL PRIMARY KEY,
    rest_config_id BIGINT REFERENCES core_service_rest(id) ON DELETE CASCADE,

    param_name VARCHAR(100) NOT NULL,
    param_value VARCHAR(500) NOT NULL
);

CREATE TABLE core_service_rest_path_param (
    id SERIAL PRIMARY KEY,
    rest_config_id BIGINT REFERENCES core_service_rest(id) ON DELETE CASCADE,

    param_name VARCHAR(100) NOT NULL,
    param_value VARCHAR(500) NOT NULL
);
