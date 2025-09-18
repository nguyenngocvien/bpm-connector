CREATE TABLE IF NOT EXISTS core_service_rest (
    id BIGINT PRIMARY KEY REFERENCES core_service_config(id) ON DELETE CASCADE,

	server_id INT REFERENCES core_servers(id) ON DELETE SET NULL,
	
    path TEXT NOT NULL,
    http_method VARCHAR(10) NOT NULL DEFAULT 'GET',
    content_type VARCHAR(50) DEFAULT 'application/json',

    timeout_ms INTEGER DEFAULT 3000,
    retry_count INTEGER DEFAULT 0,
    retry_backoff_ms INTEGER DEFAULT 1000,

    payload_template TEXT,
    response_mapping TEXT,

    headers TEXT,           -- JSON array [{"name":"Authorization","value":"Bearer token"}]
    query_params TEXT,      -- JSON array [{"name":"q","value":"value"}]
    path_params TEXT,       -- JSON array [{"name":"id","value":"123"}]

    auth_id INTEGER REFERENCES core_service_auth(id) ON DELETE SET NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);