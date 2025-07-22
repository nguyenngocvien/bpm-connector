CREATE TABLE int_log (
    id BIGSERIAL PRIMARY KEY,
    service_code VARCHAR(50),
    request_data TEXT,
    mapped_request TEXT,
    response_data TEXT,
    status_code INT,
    duration_ms INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
