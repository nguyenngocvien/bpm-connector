INSERT INTO core_service_config (
    service_code, service_type, target_url, http_method, headers, payload_template,
    log_enabled, active
) VALUES (
    'get_users', 'REST', 'https://api.example.com/users', 'GET',
    '{"Authorization": "Bearer xyz"}', NULL,
    TRUE, TRUE
);

INSERT INTO core_service_config (
    service_code, service_type, target_url, http_method, headers, payload_template,
    log_enabled, active
) VALUES (
    'create_user', 'REST', 'https://api.example.com/users', 'POST',
    '{"Content-Type": "application/json"}',
    '{"username": "$${username}", "email": "$${email}"}',
    TRUE, TRUE
);

INSERT INTO core_service_config (
    service_code, service_type, db_datasource, sql_statement, sql_type, payload_template,
    log_enabled, active
) VALUES (
    'get_employees', 'DB', 'main_ds',
    'SELECT * FROM employees WHERE department = ?',
    'QUERY',
    '["$${department}"]',
    TRUE, TRUE
);

INSERT INTO core_service_config (
    service_code, service_type, db_datasource, sql_statement, sql_type, payload_template,
    log_enabled, active
) VALUES (
    'save_order', 'DB', 'main_ds',
    'sp_save_order',
    'PROC',
    '{"in": {"orderId": "$${orderId}", "amount": "$${amount}"}, "out": ["status"]}',
    TRUE, TRUE
);

INSERT INTO core_service_config (
    service_code, service_type, target_url, payload_template,
    log_enabled, active
) VALUES (
    'send_email', 'MAIL', 'smtp.example.com:587',
    '{"to": "$${to}", "subject": "$${subject}", "body": "$${body}"}',
    TRUE, TRUE
);

INSERT INTO core_service_config (
    service_code, service_type, target_url, payload_template,
    log_enabled, active
) VALUES (
    'generate_report', 'FILE', '/tmp/reports/$${reportName}.pdf',
    '{"template": "report_template", "data": {"userId": "$${userId}"}}',
    TRUE, TRUE
);
