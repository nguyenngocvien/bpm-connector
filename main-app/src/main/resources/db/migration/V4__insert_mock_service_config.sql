INSERT INTO int_service_config (
    service_code,
    target_url,
    http_method,
    headers,
    payload_mapping,
    active,
    version,
    created_at,
    updated_at
) VALUES 
(
    'USER001',
    'http://example.com/api/users',
    'POST',
    '{"Content-Type":"application/json"}',
    '{"username":"input.name","email":"input.email"}',
    true,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    'ORDER002',
    'http://example.com/api/orders',
    'GET',
    '{"Authorization":"Bearer abc123"}',
    '{"orderId":"input.id"}',
    true,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);
